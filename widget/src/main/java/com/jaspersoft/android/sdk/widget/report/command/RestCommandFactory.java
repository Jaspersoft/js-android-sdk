package com.jaspersoft.android.sdk.widget.report.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RestCommandFactory extends SimpleCommandFactory {
    private static final String REST_TEMPLATE = "report-rest-template-v3.html";

    public RestCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client) {
        super(webView, dispatcher, eventFactory, client);
    }

    @Override
    public Command createInjectJsInterfaceCommand() {
        return new InjectJsInterfaceRestCommand(dispatcher, eventFactory, webView);
    }

    @Override
    public Command createLoadTemplateCommand() {
        return new LoadTemplateCommand(
                dispatcher,
                eventFactory,
                webView,
                client.getBaseUrl(),
                REST_TEMPLATE
        );
    }

    @Override
    public Command createInitTemplateCommand(double initialScale) {
        return new InitTemplateRestCommand(dispatcher, eventFactory);
    }

    @Override
    public Command createRunReportCommand(String reportUri) {
        ReportService reportService = ReportService.newService(client);
        return new RunReportRestCommand(dispatcher, eventFactory, webView, reportUri, reportService);
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
