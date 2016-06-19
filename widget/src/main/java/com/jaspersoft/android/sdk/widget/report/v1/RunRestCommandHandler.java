package com.jaspersoft.android.sdk.widget.report.v1;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RunRestCommandHandler implements CommandHandler<RunCommand> {
    private static final String RUN_COMMAND = "javascript:MobileClient.instance().report().run(%s);";

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

    private class Task extends AsyncTask<Object, Object, String> {
        private final RunOptions runOptions;
        private final WebView webView;
        private final double version;

        private Task(WebView webView, RunOptions runOptions, double version) {
            this.webView = webView;
            this.runOptions = runOptions;
            this.version = version;
        }

        @Override
        protected String doInBackground(Object... params) {
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

                Document document = Jsoup.parse(stream, Charset.defaultCharset().name(), "");
                Element body = document.body();
                Export exportContent = new Export(body.html());

                return String.format(RUN_COMMAND, toJson(exportContent));
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

    private static String toJson(Object object) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(object);
    }


    private static class Export {
        private final String content;

        private Export(String content) {
            this.content = content;
        }
    }
}
