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
    private final double initialScale;
    private State currentState;

    ReportRenderer(Dispatcher dispatcher,
                   StateFactory stateFactory,
                   EventPublisher eventPublisher,
                   ReportFeaturesCompat reportFeaturesCompat,
                   RenderState initialState,
                   double initialScale) {
        this.dispatcher = dispatcher;
        this.stateFactory = stateFactory;
        this.eventPublisher = eventPublisher;
        this.reportFeaturesCompat = reportFeaturesCompat;
        this.initialScale = initialScale;

        dispatcher.register(this);
        dispatcher.register(eventPublisher);

        SwapStateEvent initialStateEvent = new SwapStateEvent(initialState);
        this.onSwapState(initialStateEvent);
        eventPublisher.onSwapState(initialStateEvent);
    }

    public static ReportRenderer create(AuthorizedClient client, WebView webView, ServerInfo serverInfo, double initialScale) {
        if (serverInfo == null) {
            throw new IllegalArgumentException("ServerInfo should be provided.");
        }

        if (serverInfo.isEditionPro() && serverInfo.getVersion().greaterThanOrEquals(ServerVersion.v6)) {
            return new ReportRendererFactory.VisualizeReportRendererFactory().create(client, webView, serverInfo, initialScale);
        } else {
            return new ReportRendererFactory.RestReportRendererFactory().create(client, webView, serverInfo, initialScale);
        }
    }

    public boolean isFeatureSupported(ReportFeature reportFeature) {
        return reportFeaturesCompat.isSupported(reportFeature);
    }

    public boolean isInProgress() {
        return currentState.isInProgress();
    }

    public RenderState getRenderState() {
        return currentState.getName();
    }

    public void init() {
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
        if (currentState != null) {
            dispatcher.unregister(currentState);
        }

        switch (nextStateEvent.getNextRenderState()) {
            case IDLE:
                currentState = stateFactory.createIdleState(currentState);
                break;
            case INITED:
                currentState = stateFactory.createInitedState(currentState);
                break;
            case RENDERED:
                currentState = stateFactory.createRenderedState(currentState);
                break;
            case DESTROYED:
                currentState = stateFactory.createDestroyedState(currentState);
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