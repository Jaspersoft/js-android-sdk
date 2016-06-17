package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.JavascriptInterface;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RestJavascriptInterface {
    private final Dispatcher dispatcher;
    private final RestJavascriptEventFactory eventFactory;

    RestJavascriptInterface(Dispatcher dispatcher, RestJavascriptEventFactory eventFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
    }

    @JavascriptInterface
    public void onReportRenedered() {
        dispatcher.dispatch(eventFactory.createReportLoadedEvent());
    }

    @JavascriptInterface
    public void onWindowError(String errorLog) {
        dispatcher.dispatch(eventFactory.createWindowErrorEvent(errorLog));
    }
}
