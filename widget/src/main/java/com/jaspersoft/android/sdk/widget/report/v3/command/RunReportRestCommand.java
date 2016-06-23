package com.jaspersoft.android.sdk.widget.report.v3.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExecutionOptions;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

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
                    return parseReportDocument(reportIs);
                } catch (ServiceException|IOException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
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
                Document document = Jsoup.parse(is, Charset.defaultCharset().name(), "");
                Element body = document.body();
                return String.format(RUN_COMMAND, toJson(body.html()));
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
