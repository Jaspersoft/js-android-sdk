package com.jaspersoft.android.sdk.widget.report.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class VisualizeCommandFactory extends SimpleCommandFactory {
    private static final String VIS_TEMPLATE = "report-vis-template-v3.html";

    private final VisParamsMapper visParamsMapper;
    private final double serverVersion;

    private VisualizeCommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client, VisParamsMapper visParamsMapper, double serverVersion) {
        super(webView, dispatcher, eventFactory, client);
        this.visParamsMapper = visParamsMapper;
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
    public Command createInitTemplateCommand(double initialScale) {
        boolean isPre61 = serverVersion < 6.1;
        return new InitTemplateVisCommand(dispatcher, eventFactory, webView, client, isPre61, initialScale);
    }

    @Override
    public Command createRunReportCommand(RunOptions runOptions) {
        String reportParams = visParamsMapper.mapParams(runOptions.getParameters());
        return new RunReportVisCommand(dispatcher, eventFactory, webView, runOptions.getReportUri(), reportParams, runOptions.getDestination());
    }

    @Override
    public Command createApplyParamsCommand(List<ReportParameter> parameters) {
        String reportParams = visParamsMapper.mapParams(parameters);
        return new ApplyParamsVisCommand(dispatcher, eventFactory, webView, reportParams);
    }

    @Override
    public Command createNavigateToCommand(Destination destination) {
        return new NavigateToVisCommand(dispatcher, eventFactory, webView, destination);
    }

    static class Builder extends SimpleCommandFactory.Builder {
        private double serverVersion;

        Builder withServerVersion(double serverVersion) {
            this.serverVersion = serverVersion;
            return this;
        }

        public VisualizeCommandFactory build() {
            VisParamsMapper visParamsMapper = new VisParamsMapper();
            return new VisualizeCommandFactory(webView, dispatcher, eventFactory, client, visParamsMapper, serverVersion);
        }
    }
}
