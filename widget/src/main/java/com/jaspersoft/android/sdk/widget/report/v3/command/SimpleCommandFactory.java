package com.jaspersoft.android.sdk.widget.report.v3.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class SimpleCommandFactory {
    final WebView webView;
    final Dispatcher dispatcher;
    final EventFactory eventFactory;
    final AuthorizedClient client;

    SimpleCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client) {
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

    public Command createInitTemplateCommand() {
        throw new UnsupportedOperationException("Can not init template if engine is not defied.");
    }

    public Command createRunReportCommand(String reportUri) {
        throw new UnsupportedOperationException("Can not run report if engine is not defied.");
    }
}
