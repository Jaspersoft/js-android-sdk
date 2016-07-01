package com.jaspersoft.android.sdk.widget.report.command;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.jsinterface.JsInterface;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class CommandFactory<EV extends EventFactory> {
    final protected WebView webView;
    final protected Dispatcher dispatcher;
    final protected EV eventFactory;
    final protected AuthorizedClient client;

    protected CommandFactory(WebView webView, Dispatcher dispatcher, EV eventFactory, AuthorizedClient client) {
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

    public final Command createLoadTemplateCommand() {
        return new LoadTemplateCommand(dispatcher, eventFactory, webView, client.getBaseUrl(), provideReportTemplate());
    }

    public abstract Command createInitTemplateCommand(double initialScale);

    protected abstract String provideReportTemplate();
}
