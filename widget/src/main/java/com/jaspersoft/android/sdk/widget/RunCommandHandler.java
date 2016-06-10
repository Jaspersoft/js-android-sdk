package com.jaspersoft.android.sdk.widget;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;

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

            double version = provideVersion(client);
            SetupOptions setupOptions = SetupOptions.create(client, version);

            String settings = gson.toJson(setupOptions);
            String uri = runOptions.getUri();

            return String.format(RUN_COMMAND_SCRIPT, settings, uri);
        }

        private double provideVersion(AuthorizedClient client) {
            try {
                ServerInfoService infoService = ServerInfoService.newService(client);
                ServerInfo serverInfo = infoService.requestServerInfo();
                return serverInfo.getVersion().code();
            } catch (ServiceException e) {
                // TODO handle REST exception
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(String script) {
            webView.loadUrl(script);
        }
    }
}
