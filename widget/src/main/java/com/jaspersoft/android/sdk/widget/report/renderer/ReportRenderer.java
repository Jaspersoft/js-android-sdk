package com.jaspersoft.android.sdk.widget.report.renderer;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeature;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.renderer.event.SwapStateEvent;
import com.jaspersoft.android.sdk.widget.report.renderer.state.State;
import com.jaspersoft.android.sdk.widget.report.renderer.state.StateFactory;
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
    private final ReportFeaturesCompat reportFeaturesCompat;
    private State currentState;

    ReportRenderer(Dispatcher dispatcher, StateFactory stateFactory, State state, EventPublisher eventPublisher, ReportFeaturesCompat reportFeaturesCompat) {
        this.dispatcher = dispatcher;
        this.stateFactory = stateFactory;
        this.currentState = state;
        this.eventPublisher = eventPublisher;
        this.reportFeaturesCompat = reportFeaturesCompat;

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

    public boolean isFeatureSupported(ReportFeature reportFeature) {
        return reportFeaturesCompat.isSupported(reportFeature);
    }

    public RenderState getRenderState() {
        return currentState.getName();
    }

    public boolean isActionAvailable(ReportAction reportAction) {
        return currentState.isActionAvailable(reportAction);
    }

    public void init(double initialScale) {
        currentState.init(initialScale);
    }

    public void render(RunOptions runOptions) {
        currentState.render(runOptions);
    }

    public void applyParams(List<ReportParameter> parameters) {
        currentState.applyParams(parameters);
    }

    public void refresh() {
        currentState.refresh();
    }

    public void navigateTo(Destination destination) {
        currentState.navigateTo(destination);
    }

    public void reset() {
        currentState.reset();
    }

    public void destroy() {
        currentState.destroy();
    }

    public void registerReportRendererCallback(ReportRendererCallback reportRendererCallback) {
        eventPublisher.setReportRendererCallback(reportRendererCallback);
    }

    public void unregisterReportRendererCallback() {
        eventPublisher.setReportRendererCallback(null);
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
            case DESTROYED:
                currentState = stateFactory.createDestroyedState();
                unregisterFromDispatcher();
                return;
        }
        dispatcher.register(currentState);
    }

    private void unregisterFromDispatcher() {
        dispatcher.unregister(this);
        dispatcher.unregister(eventPublisher);
    }
}