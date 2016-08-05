package com.jaspersoft.android.sdk.widget.report.renderer.command.rest;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ExportPageRestCommand extends Command {
    private final int page;
    private final ReportExecution reportExecution;

    ExportPageRestCommand(Dispatcher dispatcher, EventFactory eventFactory, int page, ReportExecution reportExecution) {
        super(dispatcher, eventFactory);
        this.page = page;
        this.reportExecution = reportExecution;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    ReportExportOptions reportExportOptions = defineRunOptions();
                    ReportExport export = reportExecution.export(reportExportOptions);
                    ReportExportOutput output = export.download();
                    InputStream pageInputStream = output.getStream();
                    String escapedHtmlPage = parseReportDocument(pageInputStream);
                    return toJson(escapedHtmlPage);
                } catch (ServiceException e) {
                    onError(e);
                    return null;
                } catch (IOException e) {
                    ServiceException exception = new ServiceException("Failed to perform report export", e, StatusCodes.EXPORT_EXECUTION_FAILED);
                    onError(exception);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String reportPage) {
                if (reportPage != null) {
                    onExported(reportPage);
                }
            }

            private ReportExportOptions defineRunOptions() {
                return ReportExportOptions.builder()
                        .withPageRange(PageRange.parse(String.valueOf(page)))
                        .withFormat(ReportFormat.HTML)
                        .build();
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

    protected void onExported(String reportPage) {
        dispatcher.dispatch(eventFactory.createPageExportedEvent(reportPage, page));
    }

    protected void onError(ServiceException e) {
        dispatcher.dispatch(eventFactory.createErrorEvent(e));
    }
}
