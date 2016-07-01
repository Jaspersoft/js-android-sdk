package com.jaspersoft.android.sdk.widget.report;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.widget.report.command.rest.RestCommandFactory;
import com.jaspersoft.android.sdk.widget.report.command.vis.VisCommandFactory;
import com.jaspersoft.android.sdk.widget.report.command.vis.VisPre61CommandFactory;
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
            ErrorMapper errorMapper = new ErrorMapper();
            RestEventFactory eventFactory = new RestEventFactory(errorMapper);
            RestCommandFactory commandFactory = new RestCommandFactory.Creator().create(webView, dispatcher, eventFactory, client);
            RestStateFactory stateFactory = new RestStateFactory(dispatcher, eventFactory, commandFactory);
            JsInterfaceRest jsInterfaceRest = new JsInterfaceRest(dispatcher, eventFactory);
            State idleState = stateFactory.createIdleState(jsInterfaceRest);
            EventPublisher eventPublisher = new EventPublisher();
            return new ReportRenderer(dispatcher, stateFactory, idleState, eventPublisher);
        }
    }

    static class VisualizeReportRendererFactory extends ReportRendererFactory{
        @Override
        protected ReportRenderer internalCreate(AuthorizedClient client, WebView webView, ServerInfo serverInfo) {
            Dispatcher dispatcher = new Dispatcher();
            ErrorMapper errorMapper = new ErrorMapper();
            VisEventFactory eventFactory = new VisEventFactory(errorMapper);
            VisCommandFactory commandFactory;
            if (serverInfo.getVersion().lessThan(ServerVersion.v6_1)) {
                commandFactory = new VisPre61CommandFactory.Creator().create(webView, dispatcher, eventFactory, client);
            } else {
                commandFactory = new VisCommandFactory.Creator().create(webView, dispatcher, eventFactory, client);
            }
            VisStateFactory stateFactory = new VisStateFactory(dispatcher, eventFactory, commandFactory);
            HyperlinkMapper hyperlinkMapper = new HyperlinkMapper();
            JsInterfaceVis jsInterfaceVis = new JsInterfaceVis(dispatcher, eventFactory, hyperlinkMapper);
            State idleState = stateFactory.createIdleState(jsInterfaceVis);
            EventPublisher eventPublisher = new EventPublisher();
            return new ReportRenderer(dispatcher, stateFactory, idleState, eventPublisher);
        }
    }
}
