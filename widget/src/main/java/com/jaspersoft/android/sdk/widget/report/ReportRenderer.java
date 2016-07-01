package com.jaspersoft.android.sdk.widget.report;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.widget.report.event.SwapStateEvent;
import com.jaspersoft.android.sdk.widget.report.state.State;
import com.jaspersoft.android.sdk.widget.report.state.StateFactory;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportRenderer {
    private final EventPublisher eventPublisher;
    private final Dispatcher dispatcher;
    private final StateFactory stateFactory;
    private State currentState;

    ReportRenderer(Dispatcher dispatcher, StateFactory stateFactory, State state, EventPublisher eventPublisher) {
        this.dispatcher = dispatcher;
        this.stateFactory = stateFactory;
        this.currentState = state;
        this.eventPublisher = eventPublisher;

        dispatcher.register(currentState);
        dispatcher.register(this);
        dispatcher.register(eventPublisher);
    }

    public static ReportRenderer create(AuthorizedClient client, WebView webView, ServerInfo serverInfo) {
        if (serverInfo == null) {
            throw new IllegalArgumentException("ServerInfo should be provided.");
        }

        if (serverInfo.isEditionPro() && serverInfo.getVersion().greaterThanOrEquals(ServerVersion.v6)) {
            return new ReportRendererFactory.VisualizeReportRendererFactory().create(client, webView, serverInfo);
        } else {
            return new ReportRendererFactory.RestReportRendererFactory().create(client, webView, serverInfo);
        }
    }

    public static ReportRenderer restore(ReportRendererKey reportRendererKey) {
        if (reportRendererKey == null) {
            throw new IllegalArgumentException("ReportRendererKey should be provided.");
        }
        return RenderersStore.INSTANCE.restoreExecutor(reportRendererKey);
    }

    public void init(double initialScale) {
        currentState.init(initialScale);
    }

    public void run(RunOptions runOptions) {
        currentState.run(runOptions);
    }

    public void applyParams(List<ReportParameter> parameters) {
        currentState.applyParams(parameters);
    }

    public void navigateTo(Destination destination) {
        currentState.navigateTo(destination);
    }

    public void registerReportRendererCallback(ReportRendererCallback reportRendererCallback) {
        eventPublisher.setReportRendererCallback(reportRendererCallback);
    }

    public void unregisterReportRendererCallback() {
        eventPublisher.setReportRendererCallback(null);
    }

    public ReportRendererKey persist() {
        return RenderersStore.INSTANCE.saveExecutor(this);
    }

    public void destroy() {
        dispatcher.unregister(currentState);
        dispatcher.unregister(this);
        dispatcher.unregister(eventPublisher);
    }

    @Subscribe
    public void onSwapState(SwapStateEvent nextStateEvent) {
        dispatcher.unregister(currentState);
        switch (nextStateEvent.getNextRenderState()) {
            case INITED:
                currentState = stateFactory.createInitedState(currentState);
                break;
            case RENDERED:
                currentState = stateFactory.createRenderedState(currentState);
                break;
        }
        dispatcher.register(currentState);
    }
}