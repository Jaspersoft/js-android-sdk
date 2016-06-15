package com.jaspersoft.android.sdk.widget.report;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExecutionOptions;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.RunOptions;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RunRestCommandHandler implements CommandHandler<RunCommand> {
    private static final String RUN_COMMAND_SCRIPT = "javascript:MobileClient.instance().loadPage(\"%s\");";

    private AsyncTask task = DummyAsyncTask.INSTANCE;

    @Override
    public void handle(RunCommand command) {
        task = createTask(command.getOptions(), command.getVersion());
        AsyncTaskCompat.executeParallel(task);
    }

    @Override
    public void cancel() {
        task.cancel(true);
    }

    private Task createTask(RunOptions options, double version) {
        WebView webView = options.getWebView();
        return new Task(webView, options, version);
    }

    private class Task extends AsyncTask<Void, Void, String> {
        private final RunOptions runOptions;
        private final WebView webView;
        private final double version;

        private Task(WebView webView, RunOptions runOptions, double version) {
            this.webView = webView;
            this.runOptions = runOptions;
            this.version = version;
        }

        @Override
        protected String doInBackground(Void... params) {
            return buildScript(runOptions);
        }

        private String buildScript(RunOptions runOptions) {
            AuthorizedClient client = runOptions.getClient();
            ReportService reportService = ReportService.newService(client);

            String uri = runOptions.getUri();
            ReportExecutionOptions execOptions = ReportExecutionOptions.builder()
                    .withFormat(ReportFormat.HTML)
                    .withFreshData(false)
                    .withInteractive(true)
                    .withSaveSnapshot(true)
                    .build();
            try {
                ReportExecution run = reportService.run(uri, execOptions);

                ReportExportOptions exportOptions = ReportExportOptions.builder()
                        .withPageRange(PageRange.parse("1"))
                        .withFormat(ReportFormat.HTML)
                        .build();
                ReportExport export = run.export(exportOptions);
                ReportExportOutput output = export.download();
                InputStream stream = output.getStream();

                throw new UnsupportedOperationException("Not yet implemented");
            } catch (ServiceException | IOException e) {
                // Handle network exception
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String script) {
            webView.loadUrl(script);
        }
    }
}
