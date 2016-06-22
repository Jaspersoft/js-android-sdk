package com.jaspersoft.android.sdk.widget.report.v3.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.internal.SetupOptions;
import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitTemplateVisCommand extends Command {
    private static final String RUN_COMMAND_SCRIPT = "javascript:MobileClient.instance().setup(%s);";

    private final WebView webView;
    private final AuthorizedClient client;

    protected InitTemplateVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, AuthorizedClient client) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.client = client;
    }

    @Override
    protected final AsyncTask createTask() {
        return new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                SetupOptions setupOptions = SetupOptions.create(client, 6);
                String settings = toJson(setupOptions);
                return String.format(RUN_COMMAND_SCRIPT, settings);
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
}
