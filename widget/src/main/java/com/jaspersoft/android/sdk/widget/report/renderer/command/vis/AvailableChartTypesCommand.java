package com.jaspersoft.android.sdk.widget.report.renderer.command.vis;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Olexandr Dahno
 * @since 2.6
 */

public class AvailableChartTypesCommand extends Command {
    private static final String AVAILABLE_CHART_TYPES_SCRIPT = "javascript:MobileClient.getInstance().report().availableChartTypes();";

    private final WebView webView;

    AvailableChartTypesCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView) {
        super(dispatcher, eventFactory);
        this.webView = webView;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return AVAILABLE_CHART_TYPES_SCRIPT;
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
