package com.jaspersoft.android.sdk.widget.report.renderer.jsinterface;

import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.ChartType;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportComponent;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
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
    public void onReportReady() {
        dispatcher.dispatch(eventFactory.createReportReady());
    }

    @JavascriptInterface
    public void onReportComponentsChanged(String reportComponents) {
        Type listType = new TypeToken<List<ReportComponent>>(){}.getType();
        List<ReportComponent> reportComponentsList = new Gson().fromJson(reportComponents, listType);
        dispatcher.dispatch(eventFactory.createReportComponentsChanged(reportComponentsList));
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
    public void onReportPartsChanged(String reportParts) {
        Type listType = new TypeToken<List<ReportPart>>(){}.getType();
        List<ReportPart> reportPartList = new Gson().fromJson(reportParts, listType);
        dispatcher.dispatch(eventFactory.createReportPartEvent(reportPartList));
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

    @JavascriptInterface
    public void onAvailableChartTypes(String chartTypes) {
        Type listType = new TypeToken<List<ChartType>>(){}.getType();
        List<ChartType> chartTypeList = new Gson().fromJson(chartTypes, listType);
        dispatcher.dispatch(eventFactory.createAvailableChartTypesEvent(chartTypeList));
    }

    @JavascriptInterface
    public void onComponentUpdated(String componentId, String errorMessage) {
        dispatcher.dispatch(eventFactory.createComponentUpdatedEvent(componentId, errorMessage));
    }
}
