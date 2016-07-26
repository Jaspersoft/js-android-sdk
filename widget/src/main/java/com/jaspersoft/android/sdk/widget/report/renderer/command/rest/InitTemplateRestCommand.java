package com.jaspersoft.android.sdk.widget.report.renderer.command.rest;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitTemplateRestCommand extends Command {
    private static final String SETUP_COMMAND = "javascript:MobileClient.getInstance().setup(%s)";

    private final WebView webView;
    private final double scale;

    InitTemplateRestCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, double scale) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.scale = scale;
    }

    @Override
    protected final AsyncTask createTask() {
        return new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(SETUP_COMMAND, scale);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
                dispatcher.dispatch(eventFactory.createTemplateInitedEvent());
            }
        };
    }
}
