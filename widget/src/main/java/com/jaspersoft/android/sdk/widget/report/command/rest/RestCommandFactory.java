package com.jaspersoft.android.sdk.widget.report.command.rest;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RestCommandFactory extends CommandFactory<RestEventFactory> {
    private static final String REST_TEMPLATE = "report-rest-template-v3.html";

    RestCommandFactory(WebView webView, Dispatcher dispatcher, RestEventFactory eventFactory, AuthorizedClient client) {
        super(webView, dispatcher, eventFactory, client);
    }

    @Override
    public Command createInitTemplateCommand(double initialScale) {
        return new InitTemplateRestCommand(dispatcher, eventFactory);
    }

    public Command createExecuteReportCommand(RunOptions runOptions) {
        if (runOptions.getDestination().getPage() != null) {
            ReportService reportService = ReportService.newService(client);
            return new ExecuteReportRestCommand(dispatcher, eventFactory, runOptions, reportService);
        } else {
            return new AnchorRestCommand(dispatcher, eventFactory);
        }
    }

    public Command createPageExportCommand(Destination destination, ReportExecution reportExecution) {
        if (destination.getPage() != null) {
            return new ExportPageRestCommand(dispatcher, eventFactory, destination.getPage(), reportExecution);
        } else {
            return new AnchorRestCommand(dispatcher, eventFactory);
        }
    }

    public Command createShowPageCommand(String page) {
        return new ShowPageRestCommand(dispatcher, eventFactory, webView, page);
    }

    public Command createApplyParamsCommand(List<ReportParameter> parameters, ReportExecution reportExecution) {
        return new ApplyParamsRestCommand(dispatcher, eventFactory, reportExecution, parameters);
    }

    @Override
    protected String provideReportTemplate() {
        return REST_TEMPLATE;
    }

    public static class Creator {
        public RestCommandFactory create(WebView webView, Dispatcher dispatcher, RestEventFactory eventFactory, AuthorizedClient client){
            return new RestCommandFactory(webView, dispatcher, eventFactory, client);
        }
    }
}
