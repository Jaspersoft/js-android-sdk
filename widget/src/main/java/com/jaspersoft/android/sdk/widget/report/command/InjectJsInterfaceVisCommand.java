package com.jaspersoft.android.sdk.widget.report.command;

import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InjectJsInterfaceVisCommand extends InjectJsInterfaceCommand implements InjectJsInterfaceCommand.JsInterface{

    protected InjectJsInterfaceVisCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView) {
        super(dispatcher, eventFactory, webView);
    }

    @Override
    protected JsInterface provideJsInterface() {
        return this;
    }

    @JavascriptInterface
    public void onScriptReady() {
        dispatcher.dispatch(eventFactory.createTemplateInitedEvent());
    }

    @JavascriptInterface
    public void onReportRendered() {
        dispatcher.dispatch(eventFactory.createReportRenderedEvent());
    }

    @JavascriptInterface
    public void onError(String error) {
        dispatcher.dispatch(eventFactory.createErrorEvent(error));
    }

    @JavascriptInterface
    public void onHyperLinkClick(String type, String hyperlink) {
        if (type.equals("Reference")) {
            Uri referenceUri = Uri.parse(hyperlink);
            dispatcher.dispatch(eventFactory.createHyperlinkEvent(referenceUri));
        } else if (type.equals("LocalPage")) {
            int page = Integer.parseInt(hyperlink);
            dispatcher.dispatch(eventFactory.createHyperlinkEvent(page));
        } else if (type.equals("LocalAnchor")) {
            dispatcher.dispatch(eventFactory.createHyperlinkEvent(hyperlink));
        } else if (type.equals("RemotePage") || type.equals("RemoteAnchor")) {
            RemoteResource remoteHyperlink = new Gson().fromJson(hyperlink, RemoteResource.class);
            dispatcher.dispatch(eventFactory.createHyperlinkEvent(Uri.parse(remoteHyperlink.getReportUri()), remoteHyperlink.getDestination()));
        } else if (type.equals("ReportExecution")) {
            RunOptions reportExecution = new Gson().fromJson(hyperlink, RunOptions.class);
            dispatcher.dispatch(eventFactory.createHyperlinkEvent(reportExecution));
        }
    }

    private static class RemoteResource {
        private final String reportUri;
        private final Destination destination;

        public RemoteResource(String reportUri, Destination destination) {
            this.reportUri = reportUri;
            this.destination = destination;
        }

        public String getReportUri() {
            return reportUri;
        }

        public Destination getDestination() {
            return destination;
        }
    }
}
