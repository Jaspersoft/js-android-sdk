package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class WebViewProgressListener extends WebChromeClient {
    private final Dispatcher dispatcher;
    private final RunOptions options;
    private final SystemEventFactory eventFactory;

    public WebViewProgressListener(Dispatcher dispatcher, SystemEventFactory eventFactory, RunOptions options) {
        this.dispatcher = dispatcher;
        this.options = options;
        this.eventFactory = eventFactory;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);

        if (newProgress == 100) {
            view.setWebChromeClient(null);
            dispatcher.dispatch(eventFactory.createInflateCompleteEvent(options));
        }
    }
}
