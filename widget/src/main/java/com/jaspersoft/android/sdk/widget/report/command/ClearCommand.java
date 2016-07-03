package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ClearCommand extends Command {
    private static final String CLEAR_COMMAND = "javascript:MobileClient.getInstance().report().clear();";

    private final WebView webView;

    ClearCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView) {
        super(dispatcher, eventFactory);
        this.webView = webView;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return CLEAR_COMMAND;
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
