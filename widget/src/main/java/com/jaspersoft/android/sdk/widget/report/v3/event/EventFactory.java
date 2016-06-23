package com.jaspersoft.android.sdk.widget.report.v3.event;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.widget.report.v3.RenderState;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class EventFactory {
    private final Gson gson;

    public EventFactory() {
        gson = new Gson();
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
        return new ErrorEvent(exception);
    }

    public Event createErrorEvent(Exception exception) {
        return new ExceptionEvent(exception);
    }

    public Event createReportRenderedEvent() {
        return new ReportRenderedEvent();
    }
}
