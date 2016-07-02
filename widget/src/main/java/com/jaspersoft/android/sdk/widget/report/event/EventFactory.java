package com.jaspersoft.android.sdk.widget.report.event;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.JsException;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.event.rest.PageExportedEvent;
import com.jaspersoft.android.sdk.widget.report.event.rest.ParamsUpdatedEvent;
import com.jaspersoft.android.sdk.widget.report.event.rest.ReportExecutedEvent;
import com.jaspersoft.android.sdk.widget.report.hyperlink.Hyperlink;

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

    public Event createErrorEvent(String error) {
        JsException exception = new Gson().fromJson(error, JsException.class);
        ServiceException serviceException = errorMapper.map(exception);
        return new ExceptionEvent(serviceException);
    }

    public final Event createReportRenderedEvent() {
        return new ReportRenderedEvent();
    }

    public abstract Event createHyperlinkEvent(Hyperlink hyperlink);

    public abstract Event createReportExecutedEvent(ReportExecution reportExecution, Destination destination);

    public abstract Event createPageExportedEvent(String reportPage);

    public abstract Event createParamsUpdatedEvent();
}
