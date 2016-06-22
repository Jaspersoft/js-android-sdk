package com.jaspersoft.android.sdk.widget.report.v3.event;

import com.jaspersoft.android.sdk.widget.report.v3.RenderState;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class EventFactory {
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

    public Event createWindowErrorEvent(String errorLog) {
        return new WindowsErrorEvent(errorLog);
    }

    public Event createReportRenderedEvent() {
        return new ReportRenderedEvent();
    }
}
