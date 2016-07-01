package com.jaspersoft.android.sdk.widget.report.command.vis;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class NavigateToAnchorVisCommand extends Command<VisEventFactory> {
    private static final String NAVIGATE_TO_ANCHOR_SCRIPT = "javascript:MobileClient.getInstance().report().navigateTo({anchor: '%s'});";

    private final WebView webView;
    private final String anchor;

    NavigateToAnchorVisCommand(Dispatcher dispatcher, VisEventFactory eventFactory, WebView webView, String anchor) {
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
