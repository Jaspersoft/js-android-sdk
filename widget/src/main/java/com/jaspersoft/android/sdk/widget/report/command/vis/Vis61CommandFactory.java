package com.jaspersoft.android.sdk.widget.report.command.vis;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class Vis61CommandFactory extends BaseVisCommandFactory {
    private Vis61CommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client, VisParamsMapper visParamsMapper) {
        super(webView, dispatcher, eventFactory, client, visParamsMapper);
    }

    @Override
    public Command createInitTemplateCommand(double initialScale) {
        return new InitTemplateVisCommand(dispatcher, eventFactory, webView, client.getBaseUrl(), false, initialScale);
    }

    @Override
    public Command createRunReportCommand(RunOptions runOptions) {
        String reportParams = visParamsMapper.mapParams(runOptions.getParameters());
        if (runOptions.getDestination().isPageType()) {
            return new RunReportPageVisCommand(dispatcher, eventFactory, webView, runOptions.getReportUri(), reportParams, runOptions.getDestination().getPage());
        } else {
            return new RunReportAnchorVisCommand(dispatcher, eventFactory, webView, runOptions.getReportUri(), reportParams, runOptions.getDestination().getAnchor());
        }
    }

    @Override
    public Command createRefreshCommand() {
        return new RefreshVisCommand(dispatcher, eventFactory, webView);
    }

    @Override
    public Command createNavigateToCommand(Destination destination) {
        if (destination.isPageType()) {
            return new NavigateToPageVisCommand(dispatcher, eventFactory, webView, destination.getPage());
        } else {
            return new NavigateToAnchorVisCommand(dispatcher, eventFactory, webView, destination.getAnchor());
        }
    }

    public static class Creator {
        public Vis61CommandFactory create(WebView webView, Dispatcher dispatcher, VisEventFactory eventFactory, AuthorizedClient client){
            VisParamsMapper visParamsMapper = new VisParamsMapper();
            return new Vis61CommandFactory(webView, dispatcher, eventFactory, client, visParamsMapper);
        }
    }
}
