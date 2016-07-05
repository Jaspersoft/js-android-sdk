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
class RunReportPageVisCommand extends Command {
    private static final String RUN_REPORT_SCRIPT = "javascript:MobileClient.getInstance().report().run('%s', %s, %s);";

    private final WebView webView;
    private final String reportUri;
    private final String reportParams;
    private final int page;

    RunReportPageVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String reportUri, String reportParams, int page) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.reportUri = reportUri;
        this.reportParams = reportParams;
        this.page = page;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(RUN_REPORT_SCRIPT, reportUri, reportParams, page);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
