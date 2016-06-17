package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class VisWebViewProgressListener extends WebChromeClient {
    private final Dispatcher dispatcher;
    private final VisJavascriptEventFactory eventFactory;

    public VisWebViewProgressListener(Dispatcher dispatcher, VisJavascriptEventFactory eventFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);

        if (newProgress == 100) {
            view.setWebChromeClient(null);
            dispatcher.dispatch(eventFactory.createInflateCompleteEvent());
        }
    }
}
