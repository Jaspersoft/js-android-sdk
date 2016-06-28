package com.jaspersoft.android.sdk.widget.report.event;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.RenderState;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class EventFactory {
    private final Gson gson;
    private final ErrorMapper errorMapper;

    public EventFactory(ErrorMapper errorMapper) {
        this.errorMapper = errorMapper;
        gson = new Gson();
    }

    public Event createProgressStateEvent(boolean inProgress) {
        return new ProgressStateEvent(inProgress);
    }

    public Event createSwapStateEvent(RenderState nextState) {
        return new SwapStateEvent(nextState);
    }

    public Event createEngineDefinedEvent(double versionCode, boolean isPro) {
        return new EngineDefinedEvent(versionCode, isPro);
    }

    public Event createJsInterfaceInjectEvent() {
        return new JsInterfaceInjectedEvent();
    }

    public Event createTemplateLoadedEvent() {
        return new TemplateLoadedEvent();
    }

    public Event createTemplateInitedEvent() {
        return new TemplateInitedEvent();
    }

    public Event createErrorEvent(String error) {
        JsException exception = new Gson().fromJson(error, JsException.class);
        ServiceException serviceException = errorMapper.map(exception);
        return new ExceptionEvent(serviceException);
    }

    public Event createErrorEvent(ServiceException exception) {
        return new ExceptionEvent(exception);
    }

    public Event createReportRenderedEvent() {
        return new ReportRenderedEvent();
    }
}
