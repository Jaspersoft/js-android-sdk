package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebChromeClient;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class CommandFactory {
    private static final String REST_TEMPLATE = "report-rest-template.html";
    private static final String VIS_TEMPLATE = "report-vis-template.html";

    private final Dispatcher dispatcher;
    private final PresenterState.Context context;

    public CommandFactory(Dispatcher dispatcher, PresenterState.Context context) {
        this.dispatcher = dispatcher;
        this.context = context;
    }

    public Command createEngineInitCommand(RunOptions options) {
        AuthorizedClient client = context.getClient();
        ServerInfoService infoService = ServerInfoService.newService(client);
        return new InitEngineCommand(infoService, dispatcher, options);
    }

    public Command createLoadVisTemplateCommand() {
        VisJavascriptEventFactory eventFactory = new VisJavascriptEventFactory();
        VisJavascriptInterface javascriptEvents = new VisJavascriptInterface(dispatcher, eventFactory);

        AuthorizedClient client = context.getClient();
        WebChromeClient progressListener = new VisWebViewProgressListener(dispatcher, eventFactory);
        return new LoadTemplateCommand(
                context.getWebView(),
                client.getBaseUrl(),
                VIS_TEMPLATE,
                progressListener,
                javascriptEvents
        );
    }

    public Command createLoadRestTemplateCommand() {
        RestJavascriptEventFactory eventFactory = new RestJavascriptEventFactory();
        RestJavascriptInterface javascriptEvents = new RestJavascriptInterface(dispatcher, eventFactory);

        AuthorizedClient client = context.getClient();
        WebChromeClient progressListener = new RestWebViewProgressListener(dispatcher, eventFactory);
        return new LoadTemplateCommand(
                context.getWebView(),
                client.getBaseUrl(),
                REST_TEMPLATE,
                progressListener,
                javascriptEvents
        );
    }
}
