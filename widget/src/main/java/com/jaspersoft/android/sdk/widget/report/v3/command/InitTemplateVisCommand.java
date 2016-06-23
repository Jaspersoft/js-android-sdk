package com.jaspersoft.android.sdk.widget.report.v3.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitTemplateVisCommand extends Command {
    private static final String RUN_COMMAND_SCRIPT = "javascript:MobileClient.getInstance().setup(%s);";

    private final WebView webView;
    private final AuthorizedClient client;
    private final boolean pre62;

    protected InitTemplateVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, AuthorizedClient client, boolean pre62) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.client = client;
        this.pre62 = pre62;
    }

    @Override
    protected final AsyncTask createTask() {
        return new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                Server server = new Server(client.getBaseUrl(), pre62);
                String serverJson = toJson(server);
                return String.format(RUN_COMMAND_SCRIPT, serverJson);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }

    private String toJson(Object object) {
        return new Gson().toJson(object);
    }

    private static class Server {
        private final String url;
        private final boolean pre62;

        public Server(String url, boolean pre62) {
            this.url = url;
            this.pre62 = pre62;
        }
    }
}
