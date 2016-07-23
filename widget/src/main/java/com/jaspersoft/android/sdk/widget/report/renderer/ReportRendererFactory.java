package com.jaspersoft.android.sdk.widget.report.renderer;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.renderer.command.rest.RestCommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.command.vis.BaseVisCommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.command.vis.Vis61CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.BaseVisFeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.RestFeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.Vis61FeaturesCompat;
import com.jaspersoft.android.sdk.widget.report.renderer.event.rest.RestEventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.vis.VisEventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.HyperlinkMapper;
import com.jaspersoft.android.sdk.widget.report.renderer.jsinterface.JsInterface;
import com.jaspersoft.android.sdk.widget.report.renderer.jsinterface.JsInterfaceRest;
import com.jaspersoft.android.sdk.widget.report.renderer.jsinterface.JsInterfaceVis;
import com.jaspersoft.android.sdk.widget.report.renderer.state.RestStateFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.state.VisStateFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
abstract class ReportRendererFactory {

    public final ReportRenderer create(AuthorizedClient client, WebView webView, ServerInfo serverInfo, double initialScale) {
        if (webView == null) {
            throw new IllegalArgumentException("WebView should be provided.");
        }
        if (client == null) {
            throw new IllegalArgumentException("Client should be provided.");
        }

        return internalCreate(client, webView, serverInfo, initialScale);
    }

    protected abstract ReportRenderer internalCreate(AuthorizedClient client, WebView webView, ServerInfo serverInfo, double initialScale);

    static class RestReportRendererFactory extends ReportRendererFactory{
        @Override
        protected ReportRenderer internalCreate(AuthorizedClient client, WebView webView, ServerInfo serverInfo, double initialScale) {
            Dispatcher dispatcher = new Dispatcher();
            CommandExecutor commandExecutor = new CommandExecutor();
            ErrorMapper errorMapper = new ErrorMapper();
            RestEventFactory eventFactory = new RestEventFactory(errorMapper);
            RestCommandFactory commandFactory = new RestCommandFactory.Creator().create(webView, dispatcher, eventFactory, client);
            JsInterface jsInterfaceRest = new JsInterfaceRest(dispatcher, eventFactory);
            RestStateFactory stateFactory = new RestStateFactory(dispatcher, eventFactory, commandFactory, commandExecutor, jsInterfaceRest);
            EventPublisher eventPublisher = new EventPublisher();
            ReportFeaturesCompat reportFeaturesCompat = new RestFeaturesCompat();
            return new ReportRenderer(dispatcher, stateFactory, eventPublisher, reportFeaturesCompat, RenderState.IDLE, initialScale);
        }
    }

    static class VisualizeReportRendererFactory extends ReportRendererFactory{
        @Override
        protected ReportRenderer internalCreate(AuthorizedClient client, WebView webView, ServerInfo serverInfo, double initialScale) {
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
            HyperlinkMapper hyperlinkMapper = new HyperlinkMapper();
            JsInterface jsInterfaceVis = new JsInterfaceVis(dispatcher, eventFactory, hyperlinkMapper);
            VisStateFactory stateFactory = new VisStateFactory(dispatcher, eventFactory, commandFactory, commandExecutor, jsInterfaceVis);
            EventPublisher eventPublisher = new EventPublisher();
            return new ReportRenderer(dispatcher, stateFactory, eventPublisher, reportFeaturesCompat, RenderState.IDLE, initialScale);
        }
    }
}
