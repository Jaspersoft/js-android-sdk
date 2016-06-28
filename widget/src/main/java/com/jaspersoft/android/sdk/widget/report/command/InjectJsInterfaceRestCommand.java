package com.jaspersoft.android.sdk.widget.report.command;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InjectJsInterfaceRestCommand extends InjectJsInterfaceCommand implements InjectJsInterfaceCommand.JsInterface {
    protected InjectJsInterfaceRestCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView) {
        super(dispatcher, eventFactory, webView);
    }

    @Override
    protected JsInterface provideJsInterface() {
        return this;
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
