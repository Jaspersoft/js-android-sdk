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
class ShowPageRestCommand extends Command {
    private static final String SHOW_COMMAND = "javascript:MobileClient.getInstance().report().show(%s);";

    private final WebView webView;
    private final String reportPage;
    private final int pageNumber;

    ShowPageRestCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String reportPage, int pageNumber) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.reportPage = reportPage;
        this.pageNumber = pageNumber;
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
                dispatcher.dispatch(eventFactory.createCurrentPageChangedEvent(pageNumber));
            }
        };
    }
}
