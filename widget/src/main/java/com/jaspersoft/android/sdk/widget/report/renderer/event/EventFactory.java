package com.jaspersoft.android.sdk.widget.report.renderer.event;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.renderer.JsException;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

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

    public abstract Event createReportExecutedEvent(ReportExecution reportExecution, Destination destination);

    public abstract Event createPageExportedEvent(String reportPage, int pageNumber);

    public abstract Event createParamsUpdatedEvent();

    public final Event createCurrentPageChangedEvent(int currentPage) {
        return new CurrentPageChangedEvent(currentPage);
    }

    public final Event createPagesCountChangedEvent(Integer totalCount) {
        return new PagesCountChangedEvent(totalCount);
    }
}
