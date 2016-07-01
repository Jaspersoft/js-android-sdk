package com.jaspersoft.android.sdk.widget.report.jsinterface;

import android.webkit.JavascriptInterface;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class JsInterfaceRest extends JsInterface<RestEventFactory> {
    public JsInterfaceRest(Dispatcher dispatcher, RestEventFactory eventFactory) {
        super(dispatcher, eventFactory);
    }

    @JavascriptInterface
    public void onReportRendered() {
        dispatcher.dispatch(eventFactory.createReportRenderedEvent());
    }

    @JavascriptInterface
    public void onError(String error) {
        dispatcher.dispatch(eventFactory.createErrorEvent(error));
    }
}
