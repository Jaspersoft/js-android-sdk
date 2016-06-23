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
    private static final String VIS_TEMPLATE = "report-vis-template-v3.html";
    private final double serverVersion;

    VisualizeCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client, double serverVersion) {
        super(webView, dispatcher, eventFactory, client);
        this.serverVersion = serverVersion;
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
        return new InitTemplateVisCommand(dispatcher, eventFactory, webView, client, serverVersion < 6.1);
    }

    @Override
    public Command createRunReportCommand(String reportUri) {
        return new RunReportVisCommand(dispatcher, eventFactory, webView, reportUri);
    }
}
