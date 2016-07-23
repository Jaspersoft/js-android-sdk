package com.jaspersoft.android.sdk.widget.report.renderer.command.vis;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

import java.util.ArrayList;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RefreshVisCommand extends Command {
    private static final String REFRESH_SCRIPT = "javascript:MobileClient.getInstance().report().refresh();";

    private final WebView webView;

    RefreshVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView) {
        super(dispatcher, eventFactory);
        this.webView = webView;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            @Override
            protected void onPreExecute() {
                dispatcher.dispatch(eventFactory.createMultiPageStateChangedEvent(false));
                dispatcher.dispatch(eventFactory.createPagesCountChangedEvent(null));
                dispatcher.dispatch(eventFactory.createBookmarkEvent(new ArrayList<Bookmark>()));
                dispatcher.dispatch(eventFactory.createReportPartEvent(new ArrayList<ReportPart>()));
            }

            @Override
            protected String doInBackground(Object... params) {
                return REFRESH_SCRIPT;
            }

            @Override
            protected void onPostExecute(String script) {
                webView.loadUrl(script);
            }
        };
    }
}
