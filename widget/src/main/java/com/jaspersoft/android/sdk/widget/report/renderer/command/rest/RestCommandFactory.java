package com.jaspersoft.android.sdk.widget.report.renderer.command.rest;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.rest.RestEventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RestCommandFactory extends CommandFactory {
    private static final String REST_TEMPLATE = "report-rest-template.html";

    RestCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client) {
        super(webView, dispatcher, eventFactory, client);
    }

    @Override
    public Command createLoadTemplateCommand() {
        return super.createLoadTemplateCommand(REST_TEMPLATE);
    }

    @Override
    public Command createInitTemplateCommand(double initialScale) {
        return new InitTemplateRestCommand(dispatcher, eventFactory);
    }

    @Override
    public Command createRunReportCommand(RunOptions runOptions) {
        throw new UnsupportedOperationException("Run report command is not supported for rest");
    }

    @Override
    public Command createExecuteReportCommand(RunOptions runOptions) {
        if (runOptions.getDestination().isPageType()) {
            ReportService reportService = ReportService.newService(client);
            return new ExecuteReportRestCommand(dispatcher, eventFactory, runOptions, reportService);
        } else return super.createExecuteReportCommand(runOptions);
    }

    @Override
    public Command createPageExportCommand(Destination destination, ReportExecution reportExecution) {
        if (destination.isPageType()) {
            return new ExportPageRestCommand(dispatcher, eventFactory, destination.getPage(), reportExecution);
        } else return super.createPageExportCommand(destination, reportExecution);
    }

    @Override
    public Command createShowPageCommand(String page) {
        return new ShowPageRestCommand(dispatcher, eventFactory, webView, page);
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        throw new UnsupportedOperationException("Apply params command without ReportExecution command is not supported for rest");
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters, ReportExecution reportExecution) {
        return new ApplyParamsRestCommand(dispatcher, eventFactory, reportExecution, parameters);
    }

    @Override
    public Command createNavigateToCommand(Destination destination) {
        throw new UnsupportedOperationException("Navigate to command without ReportExecution command is not supported for rest");
    }

    public static class Creator {
        public RestCommandFactory create(WebView webView, Dispatcher dispatcher, RestEventFactory eventFactory, AuthorizedClient client){
            return new RestCommandFactory(webView, dispatcher, eventFactory, client);
        }
    }
}
