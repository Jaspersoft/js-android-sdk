package com.jaspersoft.android.sdk.widget.report.command;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ApplyParamsVisCommand extends Command {
    private static final String APPLY_PARAMS_SCRIPT = "javascript:MobileClient.getInstance().report().applyParams(%s);";

    private final WebView webView;
    private final String parameters;

    protected ApplyParamsVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String parameters) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.parameters = parameters;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(APPLY_PARAMS_SCRIPT, parameters);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
