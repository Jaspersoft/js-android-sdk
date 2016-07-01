package com.jaspersoft.android.sdk.widget.report.command.vis;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class VisCommandFactory extends CommandFactory<VisEventFactory> {
    private static final String VIS_TEMPLATE = "report-vis-template-v3.html";

    protected final VisParamsMapper visParamsMapper;

    VisCommandFactory(WebView webView, Dispatcher dispatcher, VisEventFactory eventFactory, AuthorizedClient client, VisParamsMapper visParamsMapper) {
        super(webView, dispatcher, eventFactory, client);

        this.visParamsMapper = visParamsMapper;
    }

    @Override
    public Command createInitTemplateCommand(double initialScale) {
        return new InitTemplateVisCommand(dispatcher, eventFactory, webView, client.getBaseUrl(), false, initialScale);
    }

    public Command createRunReportCommand(RunOptions runOptions) {
        String reportParams = visParamsMapper.mapParams(runOptions.getParameters());
        if (runOptions.getDestination().getPage() != null) {
            return new RunReportPageVisCommand(dispatcher, eventFactory, webView, runOptions.getReportUri(), reportParams, runOptions.getDestination().getPage());
        } else {
            return new RunReportAnchorVisCommand(dispatcher, eventFactory, webView, runOptions.getReportUri(), reportParams, runOptions.getDestination().getAnchor());
        }
    }

    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        String reportParams = visParamsMapper.mapParams(parameters);
        return new ApplyParamsVisCommand(dispatcher, eventFactory, webView, reportParams);
    }

    public Command createNavigateToCommand(Destination destination) {
        if (destination.getPage() != null) {
            return new NavigateToPageVisCommand(dispatcher, eventFactory, webView, destination.getPage());
        } else {
            return new NavigateToAnchorVisCommand(dispatcher, eventFactory, webView, destination.getAnchor());
        }
    }

    @Override
    protected String provideReportTemplate() {
        return VIS_TEMPLATE;
    }

    public static class Creator {
        public VisCommandFactory create(WebView webView, Dispatcher dispatcher, VisEventFactory eventFactory, AuthorizedClient client){
            VisParamsMapper visParamsMapper = new VisParamsMapper();
            return new VisCommandFactory(webView, dispatcher, eventFactory, client, visParamsMapper);
        }
    }
}
