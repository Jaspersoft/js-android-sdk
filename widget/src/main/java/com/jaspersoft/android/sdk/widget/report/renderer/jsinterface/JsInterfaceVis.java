package com.jaspersoft.android.sdk.widget.report.renderer.jsinterface;

import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.HyperlinkMapper;

import java.lang.reflect.Type;
import java.util.List;

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
    public void onDataRefreshed() {
        dispatcher.dispatch(eventFactory.createDaraRefreshedEvent());
    }

    @JavascriptInterface
    public void onParamsUpdated() {
        dispatcher.dispatch(eventFactory.createParamsUpdatedEvent());
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

    @JavascriptInterface
    public void onBookmarkListChanged(String bookmarks) {
        Type listType = new TypeToken<List<Bookmark>>(){}.getType();
        List<Bookmark> bookmarksList = new Gson().fromJson(bookmarks, listType);
        dispatcher.dispatch(eventFactory.createBookmarkEvent(bookmarksList));
    }


    @JavascriptInterface
    public void onCurrentPageChanged(int currentPage) {
        dispatcher.dispatch(eventFactory.createCurrentPageChangedEvent(currentPage));
    }

    @JavascriptInterface
    public void onPagesCountChanged(String totalCountString) {
        Integer totalCount = new Gson().fromJson(totalCountString, Integer.class);
        dispatcher.dispatch(eventFactory.createPagesCountChangedEvent(totalCount));
    }

    @JavascriptInterface
    public void onMultiPageStateChanged(boolean isMultiPage) {
        dispatcher.dispatch(eventFactory.createMultiPageStateChangedEvent(isMultiPage));
    }
}
