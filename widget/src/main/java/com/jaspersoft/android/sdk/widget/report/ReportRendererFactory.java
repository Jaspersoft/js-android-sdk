package com.jaspersoft.android.sdk.widget.report;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.widget.report.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.command.rest.RestCommandFactory;
import com.jaspersoft.android.sdk.widget.report.command.vis.BaseVisCommandFactory;
import com.jaspersoft.android.sdk.widget.report.command.vis.Vis61CommandFactory;
import com.jaspersoft.android.sdk.widget.report.compat.BaseVisFeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.compat.ReportFeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.compat.RestFeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.compat.Vis61FeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;
import com.jaspersoft.android.sdk.widget.report.hyperlink.HyperlinkMapper;
import com.jaspersoft.android.sdk.widget.report.jsinterface.JsInterfaceRest;
import com.jaspersoft.android.sdk.widget.report.jsinterface.JsInterfaceVis;
import com.jaspersoft.android.sdk.widget.report.state.RestStateFactory;
import com.jaspersoft.android.sdk.widget.report.state.State;
import com.jaspersoft.android.sdk.widget.report.state.VisStateFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
abstract class ReportRendererFactory {

    public final ReportRenderer create(AuthorizedClient client, WebView webView, ServerInfo serverInfo) {
        if (webView == null) {
            throw new IllegalArgumentException("WebView should be provided.");
        }
        if (client == null) {
            throw new IllegalArgumentException("Client should be provided.");
        }

        return internalCreate(client, webView, serverInfo);
    }

    protected abstract ReportRenderer internalCreate(AuthorizedClient client, WebView webView, ServerInfo serverInfo);

    static class RestReportRendererFactory extends ReportRendererFactory{
        @Override
        protected ReportRenderer internalCreate(AuthorizedClient client, WebView webView, ServerInfo serverInfo) {
            Dispatcher dispatcher = new Dispatcher();
            CommandExecutor commandExecutor = new CommandExecutor();
            ErrorMapper errorMapper = new ErrorMapper();
            RestEventFactory eventFactory = new RestEventFactory(errorMapper);
            RestCommandFactory commandFactory = new RestCommandFactory.Creator().create(webView, dispatcher, eventFactory, client);
            RestStateFactory stateFactory = new RestStateFactory(dispatcher, eventFactory, commandFactory, commandExecutor);
            JsInterfaceRest jsInterfaceRest = new JsInterfaceRest(dispatcher, eventFactory);
            State idleState = stateFactory.createIdleState(jsInterfaceRest);
            EventPublisher eventPublisher = new EventPublisher();
            ReportFeaturesCompat reportFeaturesCompat = new RestFeaturesCompat();
            return new ReportRenderer(dispatcher, stateFactory, idleState, eventPublisher, reportFeaturesCompat);
        }
    }

    static class VisualizeReportRendererFactory extends ReportRendererFactory{
        @Override
        protected ReportRenderer internalCreate(AuthorizedClient client, WebView webView, ServerInfo serverInfo) {
            Dispatcher dispatcher = new Dispatcher();
            CommandExecutor commandExecutor = new CommandExecutor();
            ErrorMapper errorMapper = new ErrorMapper();
            VisEventFactory eventFactory = new VisEventFactory(errorMapper);
            BaseVisCommandFactory commandFactory;
            ReportFeaturesCompat reportFeaturesCompat;
            if (serverInfo.getVersion().lessThan(ServerVersion.v6_1)) {
                commandFactory = new BaseVisCommandFactory.Creator().create(webView, dispatcher, eventFactory, client);
                reportFeaturesCompat = new BaseVisFeaturesCompat();
            } else {
                commandFactory = new Vis61CommandFactory.Creator().create(webView, dispatcher, eventFactory, client);
                reportFeaturesCompat = new Vis61FeaturesCompat();
            }
            VisStateFactory stateFactory = new VisStateFactory(dispatcher, eventFactory, commandFactory, commandExecutor);
            HyperlinkMapper hyperlinkMapper = new HyperlinkMapper();
            JsInterfaceVis jsInterfaceVis = new JsInterfaceVis(dispatcher, eventFactory, hyperlinkMapper);
            State idleState = stateFactory.createIdleState(jsInterfaceVis);
            EventPublisher eventPublisher = new EventPublisher();
            return new ReportRenderer(dispatcher, stateFactory, idleState, eventPublisher, reportFeaturesCompat);
        }
    }
}
