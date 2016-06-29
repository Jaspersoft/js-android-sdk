package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RunReportVisCommand extends Command {
    private static final String RUN_REPORT_SCRIPT = "javascript:MobileClient.getInstance().report().run('%s', %s, %s, '%s');";

    private final WebView webView;
    private final String reportUri;
    private final String reportParams;
    private final Destination destination;

    protected RunReportVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String reportUri, String reportParams, Destination destination) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.reportUri = reportUri;
        this.reportParams = reportParams;
        this.destination = destination;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(RUN_REPORT_SCRIPT, reportUri, reportParams, destination.getPage(), destination.getAnchor());
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
