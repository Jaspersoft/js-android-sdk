package com.jaspersoft.android.sdk.widget.report.renderer.command.vis;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.vis.VisEventFactory;

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
    public Command createLoadTemplateCommand() {
        return super.createLoadTemplateCommand(VIS_TEMPLATE);
    }

    @Override
    public Command createInitTemplateCommand(double initialScale) {
        return new InitTemplateVisCommand(dispatcher, eventFactory, webView, client.getBaseUrl(), true, initialScale);
    }

    @Override
    public Command createRunReportCommand(RunOptions runOptions) {
        if (runOptions.getDestination().isPageType()) {
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
    public Command createShowPageCommand(String page, int pageNumber) {
        throw new UnsupportedOperationException("Show page command is not supported for visualize");
    }

    @Override
    public Command createWaitForReportMetadataCommand(ReportExecution reportExecution) {
        throw new UnsupportedOperationException("Wait for report metadata command is not supported for visualize");
    }

    @Override
    public Command createDetectMultiPageCommand(ReportExecution reportExecution) {
        return null;
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters, ReportExecution reportExecution) {
        String reportParams = visParamsMapper.mapParams(parameters);
        return new ApplyParamsVisCommand(dispatcher, eventFactory, webView, reportParams);
    }

    @Override
    public Command createNavigateToCommand(Destination destination) {
        if (destination.isPageType()) {
            return new NavigateToPageVisCommand(dispatcher, eventFactory, webView, destination.getPage());
        } else return super.createNavigateToCommand(destination);
    }

    @Override
    public Command createRefreshCommand(ReportExecution reportExecution) {
        return super.createRefreshCommand(reportExecution);
    }

    public static class Creator {
        public BaseVisCommandFactory create(WebView webView, Dispatcher dispatcher, VisEventFactory eventFactory, AuthorizedClient client){
            VisParamsMapper visParamsMapper = new VisParamsMapper();
            return new BaseVisCommandFactory(webView, dispatcher, eventFactory, client, visParamsMapper);
        }
    }
}
