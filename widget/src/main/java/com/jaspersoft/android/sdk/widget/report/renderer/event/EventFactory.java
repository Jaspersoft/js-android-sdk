package com.jaspersoft.android.sdk.widget.report.renderer.event;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.ChartType;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.renderer.JsException;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportComponent;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class EventFactory {
    private final ErrorMapper errorMapper;

    public EventFactory(ErrorMapper errorMapper) {
        this.errorMapper = errorMapper;
    }

    public final Event createProgressStateEvent(boolean inProgress) {
        return new ProgressStateEvent(inProgress);
    }

    public final Event createSwapStateEvent(RenderState nextState) {
        return new SwapStateEvent(nextState);
    }

    public final Event createJsInterfaceInjectEvent() {
        return new JsInterfaceInjectedEvent();
    }

    public final Event createTemplateLoadedEvent() {
        return new TemplateLoadedEvent();
    }

    public final Event createTemplateInitedEvent() {
        return new TemplateInitedEvent();
    }

    public final Event createErrorEvent(ServiceException exception) {
        return new ExceptionEvent(exception);
    }

    public final Event createReportClearedEvent() {
        return new ReportClearedEvent();
    }

    public final Event createErrorEvent(String error) {
        JsException exception = new Gson().fromJson(error, JsException.class);
        ServiceException serviceException = errorMapper.map(exception);
        return new ExceptionEvent(serviceException);
    }

    public final Event createReportRenderedEvent() {
        return new ReportRenderedEvent();
    }

    public abstract Event createHyperlinkEvent(Hyperlink hyperlink);

    public final Event createBookmarkEvent(List<Bookmark> bookmarkList){
        return new BookmarksEvent(bookmarkList);
    }

    public final Event createReportPartEvent(List<ReportPart> reportPartList){
        return new ReportPartsEvent(reportPartList);
    }

    public abstract Event createReportExecutedEvent(ReportExecution reportExecution, Destination destination);

    public abstract Event createPageExportedEvent(String reportPage, int pageNumber);

    public final Event createReportReady(){
        return new ReportReadyEvent();
    }

    public final Event createReportComponentsChanged(List<ReportComponent> reportComponents) {
        return new ReportComponentsChangedEvent(reportComponents);
    }

    public final Event createCurrentPageChangedEvent(int currentPage) {
        return new CurrentPageChangedEvent(currentPage);
    }

    public final Event createPagesCountChangedEvent(Integer totalCount) {
        return new PagesCountChangedEvent(totalCount);
    }

    public final Event createMultiPageStateChangedEvent(boolean isMultiPage) {
        return new MultiPageStateChangedEvent(isMultiPage);
    }

    public final Event createAvailableChartTypesEvent(List<ChartType> chartTypes) {
        return new AvailableChartTypesEvent(chartTypes);
    }
}
