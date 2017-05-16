package com.jaspersoft.android.sdk.widget.report.renderer.command.rest;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ShowPageRestCommand extends Command {
    private static final String SHOW_COMMAND = "javascript:MobileClient.getInstance().report().show(%s, %s, %s);";

    private final WebView webView;
    private final String reportPage;
    private final String executionId;
    private final boolean isPro;
    private final int pageNumber;

    ShowPageRestCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String reportPage, String executionId, boolean isPro, int pageNumber) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.reportPage = reportPage;
        this.executionId = executionId;
        this.isPro = isPro;
        this.pageNumber = pageNumber;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected String doInBackground(Object... params) {
                RequestData requestData = new RequestData(executionId, pageNumber - 1);
                String json = new Gson().toJson(requestData);
                return String.format(SHOW_COMMAND, reportPage, isPro, json);
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
                dispatcher.dispatch(eventFactory.createCurrentPageChangedEvent(pageNumber));
            }
        };
    }

    private class RequestData {
        private final String jasperPrintName;
        private final int pageIndex;

        private RequestData(String jasperPrintName, int pageIndex) {
            this.jasperPrintName = jasperPrintName;
            this.pageIndex = pageIndex;
        }
    }
}
