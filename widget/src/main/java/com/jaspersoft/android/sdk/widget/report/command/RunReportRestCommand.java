package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExecutionOptions;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RunReportRestCommand extends Command {
    private static final String RUN_COMMAND = "javascript:MobileClient.getInstance().report().show(%s);";

    private final WebView webView;
    private final String reportUri;
    private final ReportService reportService;

    protected RunReportRestCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String reportUri, ReportService reportService) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.reportUri = reportUri;
        this.reportService = reportService;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                ReportExecutionOptions execOptions = defineRunOptions();

                try {
                    InputStream reportIs = exportReport(execOptions);
                    String reportHtml = parseReportDocument(reportIs);

                    return String.format(RUN_COMMAND, toJson(reportHtml));
                } catch (ServiceException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
                } catch (IOException e) {
                    ServiceException exception = new ServiceException("Can not render report", e, StatusCodes.REPORT_RENDER_FAILED);
                    dispatcher.dispatch(eventFactory.createErrorEvent(exception));
                }
                return null;
            }

            @Override
            protected void onPostExecute(String script) {
                if (script == null) return;
                webView.loadUrl(script);
            }

            private ReportExecutionOptions defineRunOptions() {
                return ReportExecutionOptions.builder()
                        .withFormat(ReportFormat.HTML)
                        .withFreshData(false)
                        .withInteractive(true)
                        .withSaveSnapshot(true)
                        .build();
            }

            private InputStream exportReport(ReportExecutionOptions execOptions) throws ServiceException, IOException {
                ReportExecution run = reportService.run(reportUri, execOptions);

                ReportExportOptions exportOptions = ReportExportOptions.builder()
                        .withPageRange(PageRange.parse("1"))
                        .withFormat(ReportFormat.HTML)
                        .build();
                ReportExport export = run.export(exportOptions);
                ReportExportOutput output = export.download();
                return output.getStream();
            }

            private String parseReportDocument(InputStream is) throws IOException {
                StringBuilder htmlBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                String line = bufferedReader.readLine();
                while (line != null) {
                    htmlBuilder.append(line);
                    htmlBuilder.append('\n');
                    line = bufferedReader.readLine();
                }

                return htmlBuilder.toString();
            }

            private String toJson(Object object) {
                Gson gson = new GsonBuilder()
                        .disableHtmlEscaping()
                        .create();
                return gson.toJson(object);
            }
        };
    }
}
