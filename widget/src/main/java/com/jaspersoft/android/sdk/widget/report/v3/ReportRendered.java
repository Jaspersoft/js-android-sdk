package com.jaspersoft.android.sdk.widget.report.v3;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.report.v3.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.SwapStateEvent;
import com.jaspersoft.android.sdk.widget.report.v3.state.State;
import com.jaspersoft.android.sdk.widget.report.v3.state.StateFactory;
import com.squareup.otto.Subscribe;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportRendered {
    private final EventPublisher eventPublisher;
    private final Dispatcher dispatcher;
    private final StateFactory stateFactory;
    private State currentState;

    ReportRendered(Dispatcher dispatcher, StateFactory stateFactory, State state) {
        this.dispatcher = dispatcher;
        this.stateFactory = stateFactory;
        this.currentState = state;
        eventPublisher = new EventPublisher();

        dispatcher.register(currentState);
        dispatcher.register(this);
        dispatcher.register(eventPublisher);
    }

    public void init() {
        currentState.init();
    }

    public void run() {
        currentState.run();
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
        currentState = stateFactory.createInitedState();
        dispatcher.register(currentState);
    }

    public static class Builder {
        private AuthorizedClient client;
        private WebView webView;
        private ReportRendererKey reportRendererKey;

        public CreateBuilder withWebView(WebView webView) {
            this.webView = webView;
            return new CreateBuilder();
        }

        public RestoreBuilder withKey(ReportRendererKey reportRendererKey) {
            this.reportRendererKey = reportRendererKey;
            return new RestoreBuilder();
        }

        public CreateBuilder withClient(AuthorizedClient client) {
            this.client = client;
            return new CreateBuilder();
        }

        public class CreateBuilder {
            public CreateBuilder withWebView(WebView webView) {
                Builder.this.webView = webView;
                return new CreateBuilder();
            }

            public CreateBuilder withClient(AuthorizedClient client) {
                Builder.this.client = client;
                return new CreateBuilder();
            }

            public ReportRendered build() {
                if (webView == null) {
                    throw new IllegalArgumentException("WebView should be provided.");
                }
                if (client == null) {
                    throw new IllegalArgumentException("Client should be provided.");
                }
                return createRenderer();
            }
        }

        public class RestoreBuilder {
            public ReportRendered build() {
                if (reportRendererKey == null) {
                    throw new IllegalArgumentException("Report renderer key should be provided.");
                }

                return RenderersStore.INSTANCE.restoreExecutor(reportRendererKey);
            }
        }

        private ReportRendered createRenderer() {
            Dispatcher dispatcher = new Dispatcher();
            EventFactory eventFactory = new EventFactory();
            CommandFactory commandFactory = new CommandFactory.Builder()
                    .setWebView(webView)
                    .setClient(client)
                    .setDispatcher(dispatcher)
                    .setEventFactory(eventFactory)
                    .build();
            StateFactory stateFactory = new StateFactory(dispatcher, eventFactory, commandFactory);
            State idleState = stateFactory.createIdleState();
            return new ReportRendered(dispatcher, stateFactory, idleState);
        }
    }
}