package com.jaspersoft.android.sdk.widget.report.renderer.command.vis;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.renderer.ChartType;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportComponent;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Olexandr Dahno
 * @since 2.6
 */

public class UpdateChartTypeCommand extends Command {
    private static final String UPDATE_CHART_TYPE_SCRIPT = "javascript:MobileClient.getInstance().report().updateChartType('%s', '%s');";

    private final WebView webView;
    private ReportComponent mReportComponent;
    private ChartType mChartType;

    UpdateChartTypeCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, ReportComponent component, ChartType newChartType) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        mReportComponent = component;
        mChartType = newChartType;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                return String.format(UPDATE_CHART_TYPE_SCRIPT, mReportComponent.getId(), mChartType.getName());
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
