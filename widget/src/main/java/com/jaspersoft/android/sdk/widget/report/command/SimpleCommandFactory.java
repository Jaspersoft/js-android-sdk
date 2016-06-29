package com.jaspersoft.android.sdk.widget.report.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class SimpleCommandFactory {
    final WebView webView;
    final Dispatcher dispatcher;
    final EventFactory eventFactory;
    final AuthorizedClient client;

    protected SimpleCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client) {
        this.webView = webView;
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.client = client;
    }

    public final Command createDefineEngineCommand() {
        ServerInfoService infoService = ServerInfoService.newService(client);
        return new DefineEngineCommand(dispatcher, eventFactory, infoService);
    }

    public Command createInjectJsInterfaceCommand() {
        throw new UnsupportedOperationException("Can not inject JS interface if engine is not defied.");
    }

    public Command createLoadTemplateCommand() {
        throw new UnsupportedOperationException("Can not create template if engine is not defied.");
    }

    public Command createInitTemplateCommand(double initialScale) {
        throw new UnsupportedOperationException("Can not init template if engine is not defied.");
    }

    public Command createRunReportCommand(RunOptions runOptions) {
        throw new UnsupportedOperationException("Can not run report if engine is not defied.");
    }

    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        throw new UnsupportedOperationException("Can not apply params if engine is not defied.");
    }

    static class Builder{
        protected WebView webView;
        protected Dispatcher dispatcher;
        protected EventFactory eventFactory;
        protected AuthorizedClient client;

        public Builder withWebView(WebView webView) {
            this.webView = webView;
            return this;
        }

        public Builder withDispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder withEventFactory(EventFactory eventFactory) {
            this.eventFactory = eventFactory;
            return this;
        }

        public Builder withClient(AuthorizedClient client) {
            this.client = client;
            return this;
        }

        public SimpleCommandFactory build() {
            return new SimpleCommandFactory(webView, dispatcher, eventFactory, client);
        }
    }
}
