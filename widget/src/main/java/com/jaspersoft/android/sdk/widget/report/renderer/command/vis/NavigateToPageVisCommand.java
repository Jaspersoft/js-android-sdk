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
public class NavigateToPageVisCommand extends Command {
    private static final String NAVIGATE_TO_SCRIPT = "javascript:MobileClient.getInstance().report().navigateTo(%s);";
    private final WebView webView;
    private final int page;

    protected NavigateToPageVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, int page) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.page = page;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(NAVIGATE_TO_SCRIPT, page);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
