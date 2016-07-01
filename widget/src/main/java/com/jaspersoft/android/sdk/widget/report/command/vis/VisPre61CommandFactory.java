package com.jaspersoft.android.sdk.widget.report.command.vis;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class VisPre61CommandFactory extends VisCommandFactory {

    private VisPre61CommandFactory(WebView webView, Dispatcher dispatcher, VisEventFactory eventFactory, AuthorizedClient client, VisParamsMapper visParamsMapper) {
        super(webView, dispatcher, eventFactory, client, visParamsMapper);
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
        } else {
            return new Pre61AnchorVisCommand(dispatcher, eventFactory);
        }
    }

    public Command createNavigateToCommand(Destination destination) {
        if (destination.getPage() != null) {
            return new NavigateToPageVisCommand(dispatcher, eventFactory, webView, destination.getPage());
        } else {
            return new Pre61AnchorVisCommand(dispatcher, eventFactory);
        }
    }

    public static class Creator {
        public VisPre61CommandFactory create(WebView webView, Dispatcher dispatcher, VisEventFactory eventFactory, AuthorizedClient client){
            VisParamsMapper visParamsMapper = new VisParamsMapper();
            return new VisPre61CommandFactory(webView, dispatcher, eventFactory, client, visParamsMapper);
        }
    }
}
