package com.jaspersoft.android.sdk.widget.report.renderer.command.vis;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class NavigateToAnchorVisCommand extends Command {
    private static final String NAVIGATE_TO_ANCHOR_SCRIPT = "javascript:MobileClient.getInstance().report().navigateTo({anchor: '%s'});";

    private final WebView webView;
    private final String anchor;

    NavigateToAnchorVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String anchor) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.anchor = anchor;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(NAVIGATE_TO_ANCHOR_SCRIPT, anchor);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
