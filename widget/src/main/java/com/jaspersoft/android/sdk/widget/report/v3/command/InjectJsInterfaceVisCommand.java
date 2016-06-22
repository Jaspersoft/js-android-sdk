package com.jaspersoft.android.sdk.widget.report.v3.command;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InjectJsInterfaceVisCommand extends InjectJsInterfaceCommand implements InjectJsInterfaceCommand.JsInterface{

    protected InjectJsInterfaceVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView) {
        super(dispatcher, eventFactory, webView);
    }

    @Override
    protected JsInterface provideJsInterface() {
        return this;
    }

    @JavascriptInterface
    public void onScriptReady() {
        dispatcher.dispatch(eventFactory.createTemplateInitedEvent());
    }

    @JavascriptInterface
    public void onReportRenedered() {
        dispatcher.dispatch(eventFactory.createReportRenderedEvent());
    }

    @JavascriptInterface
    public void onWindowError(String errorLog) {
        dispatcher.dispatch(eventFactory.createWindowErrorEvent(errorLog));
    }

    @JavascriptInterface
    public void onHyperLinkClick(String linkData) {
    }
}
