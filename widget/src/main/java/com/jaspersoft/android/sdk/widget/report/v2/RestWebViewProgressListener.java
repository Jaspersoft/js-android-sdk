package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RestWebViewProgressListener extends WebChromeClient {
    private final Dispatcher dispatcher;
    private final RestJavascriptEventFactory eventFactory;

    public RestWebViewProgressListener(Dispatcher dispatcher, RestJavascriptEventFactory eventFactory) {
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
