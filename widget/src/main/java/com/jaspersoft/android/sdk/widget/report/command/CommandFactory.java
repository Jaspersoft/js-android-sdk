package com.jaspersoft.android.sdk.widget.report.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class CommandFactory {
    private SimpleCommandFactory simpleCommandFactory;

    public CommandFactory(SimpleCommandFactory simpleCommandFactory) {
        this.simpleCommandFactory = simpleCommandFactory;
    }

    public final void updateServerMetadata(double versionCode, boolean isPro) {
        if (isPro && versionCode >= 6.0) {
            simpleCommandFactory = new VisualizeCommandFactory.Builder()
                    .withServerVersion(versionCode)
                    .withWebView(simpleCommandFactory.webView)
                    .withDispatcher(simpleCommandFactory.dispatcher)
                    .withEventFactory(simpleCommandFactory.eventFactory)
                    .withClient(simpleCommandFactory.client)
                    .build();
        } else {
            simpleCommandFactory = new RestCommandFactory.Builder()
                    .withWebView(simpleCommandFactory.webView)
                    .withDispatcher(simpleCommandFactory.dispatcher)
                    .withEventFactory(simpleCommandFactory.eventFactory)
                    .withClient(simpleCommandFactory.client)
                    .build();
        }
    }

    public final Command createDefineEngineCommand() {
        return simpleCommandFactory.createDefineEngineCommand();
    }

    public Command createInjectJsInterfaceCommand() {
        return simpleCommandFactory.createInjectJsInterfaceCommand();
    }

    public Command createLoadTemplateCommand() {
        return simpleCommandFactory.createLoadTemplateCommand();
    }

    public Command createInitTemplateCommand(double initialScale) {
        return simpleCommandFactory.createInitTemplateCommand(initialScale);
    }

    public Command createRunReportCommand(RunOptions runOptions) {
        return simpleCommandFactory.createRunReportCommand(runOptions);
    }

    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        return simpleCommandFactory.createApplyParamsCommand(parameters);
    }

    public Command createNavigateToCommand(Destination destination) {
        return simpleCommandFactory.createNavigateToCommand(destination);
    }

    public static class Builder {
        private WebView webView;
        private Dispatcher dispatcher;
        private EventFactory eventFactory;
        private AuthorizedClient client;

        public Builder setDispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder setEventFactory(EventFactory eventFactory) {
            this.eventFactory = eventFactory;
            return this;
        }

        public Builder setClient(AuthorizedClient client) {
            this.client = client;
            return this;
        }

        public Builder setWebView(WebView webView) {
            this.webView = webView;
            return this;
        }

        public CommandFactory build() {
            checkArguments();
            SimpleCommandFactory commandFactory = new SimpleCommandFactory(webView, dispatcher, eventFactory, client);
            return new CommandFactory(commandFactory);
        }

        private void checkArguments() {
            if (dispatcher == null) {
                throw new IllegalArgumentException("Dispatcher should be provided.");
            }
            if (eventFactory == null) {
                throw new IllegalArgumentException("EventFactory should be provided.");
            }
            if (client == null) {
                throw new IllegalArgumentException("AuthorizedClient should be provided.");
            }
        }
    }
}
