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
public class NavigateToVisCommand extends Command {
    private static final String NAVIGATE_TO_SCRIPT = "javascript:MobileClient.getInstance().report().navigateTo(%s, '%s');";
    private final WebView webView;
    private final Destination destination;

    protected NavigateToVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, Destination destination) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.destination = destination;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(NAVIGATE_TO_SCRIPT, destination.getPage(), destination.getAnchor());
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
