package com.jaspersoft.android.sdk.widget.report.renderer.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.jsinterface.JsInterface;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class CommandFactory {
    final protected WebView webView;
    final protected Dispatcher dispatcher;
    final protected EventFactory eventFactory;
    final protected AuthorizedClient client;

    protected CommandFactory(WebView webView, Dispatcher dispatcher, EventFactory eventFactory, AuthorizedClient client) {
        if (dispatcher == null) {
            throw new IllegalArgumentException("Dispatcher should be provided.");
        }
        if (eventFactory == null) {
            throw new IllegalArgumentException("EventFactory should be provided.");
        }
        if (client == null) {
            throw new IllegalArgumentException("AuthorizedClient should be provided.");
        }
        if (webView == null) {
            throw new IllegalArgumentException("WebView should be provided.");
        }
        this.webView = webView;
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.client = client;
    }

    public final Command createInjectJsInterfaceCommand(JsInterface jsInterface) {
        return new InjectJsInterfaceCommand( dispatcher, eventFactory, webView, jsInterface);
    }

    public abstract Command createLoadTemplateCommand();

    public final Command createLoadTemplateCommand(String reportTemplate) {
        return new LoadTemplateCommand(dispatcher, eventFactory, webView, client.getBaseUrl(), reportTemplate);
    }

    public final Command createResetCommand() {
        return new ResetCommand(dispatcher, eventFactory, webView);
    }

    public abstract Command createInitTemplateCommand(double initialScale);

    public Command createRunReportCommand(RunOptions runOptions){
        return new AnchorUnsupportedCommand(dispatcher, eventFactory);
    }

    public Command createExecuteReportCommand(RunOptions runOptions){
        return new AnchorUnsupportedCommand(dispatcher, eventFactory);
    }

    public Command createPageExportCommand(Destination destination, ReportExecution reportExecution){
        return new AnchorUnsupportedCommand(dispatcher, eventFactory);
    }

    public abstract Command createShowPageCommand(String page);

    public abstract Command createApplyParamsCommand(List<ReportParameter> parameters);

    public abstract Command createApplyParamsCommand(List<ReportParameter> parameters, ReportExecution reportExecution);

    public Command createNavigateToCommand(Destination destination) {
        return new AnchorUnsupportedCommand(dispatcher, eventFactory);
    }

    public Command createRefreshCommand() {
       return new RefreshUnsupportedCommand(dispatcher, eventFactory);
    }
}
