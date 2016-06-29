package com.jaspersoft.android.sdk.widget.report.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RestCommandFactory extends SimpleCommandFactory {
    private static final String REST_TEMPLATE = "report-rest-template-v3.html";
    private final ReportExecutor reportExecutor;

    private RestCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client, ReportExecutor reportExecutor) {
        super(webView, dispatcher, eventFactory, client);
        this.reportExecutor = reportExecutor;
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
    public Command createRunReportCommand(RunOptions runOptions) {
        ReportService reportService = ReportService.newService(client);
        return new RunReportRestCommand(dispatcher, eventFactory, webView, runOptions, reportExecutor);
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        return new ApplyParamsRestCommand(dispatcher, eventFactory, webView, reportExecutor, parameters);
    }

    static class Builder extends SimpleCommandFactory.Builder {
        public RestCommandFactory build() {
            ReportService reportService = ReportService.newService(client);
            ReportExecutor reportExecutor = new ReportExecutor(reportService);
            return new RestCommandFactory(webView, dispatcher, eventFactory, client, reportExecutor);
        }
    }
}
