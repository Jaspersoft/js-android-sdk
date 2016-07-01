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
class ApplyParamsVisCommand extends Command<VisEventFactory> {
    private static final String APPLY_PARAMS_SCRIPT = "javascript:MobileClient.getInstance().report().applyParams(%s);";

    private final WebView webView;
    private final String parameters;

    ApplyParamsVisCommand(Dispatcher dispatcher, VisEventFactory eventFactory, WebView webView, String parameters) {
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
