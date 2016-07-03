package com.jaspersoft.android.sdk.widget.report.jsinterface;

import android.webkit.JavascriptInterface;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;
import com.jaspersoft.android.sdk.widget.report.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.HyperlinkMapper;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class JsInterfaceVis extends JsInterface {

    private final HyperlinkMapper hyperlinkMapper;

    public JsInterfaceVis(Dispatcher dispatcher, EventFactory eventFactory, HyperlinkMapper hyperlinkMapper) {
        super(dispatcher, eventFactory);
        this.hyperlinkMapper = hyperlinkMapper;
    }

    @JavascriptInterface
    public void onInited() {
        dispatcher.dispatch(eventFactory.createTemplateInitedEvent());
    }

    @JavascriptInterface
    public void onRendered() {
        dispatcher.dispatch(eventFactory.createReportRenderedEvent());
    }

    @JavascriptInterface
    public void onError(String error) {
        dispatcher.dispatch(eventFactory.createErrorEvent(error));
    }

    @JavascriptInterface
    public void onCleared() {
        dispatcher.dispatch(eventFactory.createReportClearedEvent());
    }

    @JavascriptInterface
    public void onHyperLinkClick(String type, String hyperlinkParam) {
        Hyperlink hyperlink = hyperlinkMapper.map(type, hyperlinkParam);
        dispatcher.dispatch(eventFactory.createHyperlinkEvent(hyperlink));
    }
}
