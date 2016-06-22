package com.jaspersoft.android.sdk.widget.report.v3.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class VisualizeCommandFactory extends SimpleCommandFactory {
    private static final String VIS_TEMPLATE = "report-vis-template.html";

    VisualizeCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client) {
        super(webView, dispatcher, eventFactory, client);
    }

    @Override
    public Command createInjectJsInterfaceCommand() {
        return new InjectJsInterfaceVisCommand(dispatcher, eventFactory, webView);
    }

    @Override
    public Command createLoadTemplateCommand() {
        return new LoadTemplateCommand(
                dispatcher,
                eventFactory,
                webView,
                client.getBaseUrl(),
                VIS_TEMPLATE
        );
    }

    @Override
    public Command createInitTemplateCommand() {
        return new InitTemplateVisCommand(dispatcher, eventFactory, webView, client);
    }
}
