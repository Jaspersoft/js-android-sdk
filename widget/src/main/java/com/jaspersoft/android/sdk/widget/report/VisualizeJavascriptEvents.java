package com.jaspersoft.android.sdk.widget.report;

import android.webkit.JavascriptInterface;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class VisualizeJavascriptEvents {
    private final Dispatcher dispatcher;
    private final Event.Factory eventFactory;

    VisualizeJavascriptEvents(Dispatcher dispatcher, Event.Factory eventFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
    }

    @JavascriptInterface
    public void onVisualizeReady() {
        dispatcher.dispatch(eventFactory.createScriptLoadedEvent());
    }

    @JavascriptInterface
    public void onReportRenedered() {
        dispatcher.dispatch(eventFactory.createReportLoadedEvent());
    }
}
