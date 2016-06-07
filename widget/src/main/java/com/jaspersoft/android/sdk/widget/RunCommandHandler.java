package com.jaspersoft.android.sdk.widget;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RunCommandHandler implements CommandHandler<RunCommand> {
    private static final String RUN_COMMAND_SCRIPT = "javascript:MobileClient.instance().setup(%s,\n" +
            "function (dashboard) {\n" +
            "dashboard.run(\"%s\");\n" +
            "});";

    @Override
    public void handle(RunCommand command) {
        Task runTask = new Task(command.getOptions());
        AsyncTaskCompat.executeParallel(runTask);
    }

    private class Task extends AsyncTask<Void, Void, String> {
        private final RunOptions runOptions;
        private final WebView webView;

        private Task(RunOptions runOptions) {
            this.runOptions = runOptions;
            this.webView = runOptions.getWebView();
        }

        @Override
        protected String doInBackground(Void... params) {
            return buildScript(runOptions);
        }

        private String buildScript(RunOptions runOptions) {
            Gson gson = new Gson();
            AuthorizedClient client = runOptions.getClient();
            SetupOptions setupOptions = SetupOptions.from(client);

            String settings = gson.toJson(setupOptions);
            String uri = runOptions.getUri();

            return String.format(RUN_COMMAND_SCRIPT, settings, uri);
        }

        @Override
        protected void onPostExecute(String script) {
            webView.loadUrl(script);
        }
    }
}
