package com.jaspersoft.android.sdk.widget.report.command.vis;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class BaseVisCommandFactory extends CommandFactory {
    private static final String VIS_TEMPLATE = "report-vis-template.html";

    protected final VisParamsMapper visParamsMapper;

    BaseVisCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client, VisParamsMapper visParamsMapper) {
        super(webView, dispatcher, eventFactory, client);

        this.visParamsMapper = visParamsMapper;
    }

    @Override
    public Command createInitTemplateCommand(double initialScale) {
        return new InitTemplateVisCommand(dispatcher, eventFactory, webView, client.getBaseUrl(), true, initialScale);
    }

    @Override
    public Command createRunReportCommand(RunOptions runOptions) {
        if (runOptions.getDestination().getPage() != null) {
            String reportParams = visParamsMapper.mapParams(runOptions.getParameters());
            return new RunReportPageVisCommand(dispatcher, eventFactory, webView, runOptions.getReportUri(), reportParams, runOptions.getDestination().getPage());
        } else return super.createRunReportCommand(runOptions);
    }

    @Override
    public Command createExecuteReportCommand(RunOptions runOptions) {
        throw new UnsupportedOperationException("Execute report command is not supported for visualize");
    }

    @Override
    public Command createPageExportCommand(Destination destination, ReportExecution reportExecution) {
        throw new UnsupportedOperationException("Export page command is not supported for visualize");
    }

    @Override
    public Command createShowPageCommand(String page) {
        throw new UnsupportedOperationException("Show page command is not supported for visualize");
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        String reportParams = visParamsMapper.mapParams(parameters);
        return new ApplyParamsVisCommand(dispatcher, eventFactory, webView, reportParams);
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters, ReportExecution reportExecution) {
        return createApplyParamsCommand(parameters);
    }

    @Override
    public Command createNavigateToCommand(Destination destination) {
        if (destination.getPage() != null) {
            return new NavigateToPageVisCommand(dispatcher, eventFactory, webView, destination.getPage());
        } else return super.createNavigateToCommand(destination);
    }

    @Override
    public Command createRefreshCommand() {
        return super.createRefreshCommand();
    }

    @Override
    protected String provideReportTemplate() {
        return VIS_TEMPLATE;
    }

    public static class Creator {
        public BaseVisCommandFactory create(WebView webView, Dispatcher dispatcher, VisEventFactory eventFactory, AuthorizedClient client){
            VisParamsMapper visParamsMapper = new VisParamsMapper();
            return new BaseVisCommandFactory(webView, dispatcher, eventFactory, client, visParamsMapper);
        }
    }
}
