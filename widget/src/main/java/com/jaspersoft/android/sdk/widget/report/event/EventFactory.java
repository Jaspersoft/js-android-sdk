package com.jaspersoft.android.sdk.widget.report.event;

import android.net.Uri;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.LocalHyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.ReferenceHyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.RemoteHyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.ReportExecutionHyperlink;

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

    public Event createHyperlinkEvent(Uri reference) {
        Hyperlink hyperlink = new ReferenceHyperlink(reference);
        return new HyperlinkEvent(hyperlink);
    }

    public Event createHyperlinkEvent(int page) {
        Hyperlink hyperlink = new LocalHyperlink(new Destination(page));
        return new HyperlinkEvent(hyperlink);
    }

    public Event createHyperlinkEvent(String anchor) {
        Hyperlink hyperlink = new LocalHyperlink(new Destination(anchor));
        return new HyperlinkEvent(hyperlink);
    }

    public Event createHyperlinkEvent(Uri resourceUri, Destination destination) {
        Hyperlink hyperlink = new RemoteHyperlink(resourceUri, destination);
        return new HyperlinkEvent(hyperlink);
    }

    public Event createHyperlinkEvent(RunOptions runOptions) {
        Hyperlink hyperlink = new ReportExecutionHyperlink(runOptions);
        return new HyperlinkEvent(hyperlink);
    }

    public Event createReportRenderedEvent() {
        return new ReportRenderedEvent();
    }
}
