package com.jaspersoft.android.sdk.widget.report;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.internal.SetupOptions;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RunVisualizeCommandHandler implements CommandHandler<RunCommand> {
    private static final String RUN_COMMAND_SCRIPT = "javascript:MobileClient.instance().setup(%s,\n" +
            "function (report) {\n" +
            "report.run(\"%s\");\n" +
            "});";

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

        private Task(WebView webView, RunOptions runOptions,  double version) {
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

            SetupOptions setupOptions = SetupOptions.create(client, version);

            String settings = toJson(setupOptions);
            String uri = runOptions.getUri();

            return String.format(RUN_COMMAND_SCRIPT, settings, uri);
        }

        private String toJson(Object object) {
            return new Gson().toJson(object);
        }

        @Override
        protected void onPostExecute(String script) {
            webView.loadUrl(script);
        }
    }
}
