package com.jaspersoft.android.sdk.widget.report.command.rest;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ShowPageRestCommand extends Command<RestEventFactory> {
    private static final String SHOW_COMMAND = "javascript:MobileClient.getInstance().report().show(%s);";

    private final WebView webView;
    private final String reportPage;

    ShowPageRestCommand(Dispatcher dispatcher, RestEventFactory eventFactory, WebView webView, String reportPage) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.reportPage = reportPage;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(SHOW_COMMAND, reportPage);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
