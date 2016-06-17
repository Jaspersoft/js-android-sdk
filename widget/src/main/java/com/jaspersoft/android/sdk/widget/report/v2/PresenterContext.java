package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class PresenterContext implements PresenterState.Context {
    private final String uri;
    private final WebView webView;
    private final AuthorizedClient client;
    private final PresenterListeners listeners;
    private final Dispatcher dispatcher;

    private PresenterState state;
    private CommandFactory commandFactory;
    private StateFactory stateFactory;

    public PresenterContext(
            String uri,
            WebView webView,
            AuthorizedClient client,
            PresenterListeners listeners,
            Dispatcher dispatcher
    ) {
        this.uri = uri;
        this.webView = webView;
        this.client = client;
        this.listeners = listeners;
        this.dispatcher = dispatcher;
        this.state = provideStateFactory().createInitState();
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public WebView getWebView() {
        return webView;
    }

    @Override
    public AuthorizedClient getClient() {
        return client;
    }

    @Override
    public PresenterState getCurrentState() {
        return state;
    }

    @Override
    public PresenterListeners getListeners() {
        return listeners;
    }

    @Override
    public CommandFactory provideCommandFactory() {
        if (commandFactory == null) {
            commandFactory = new CommandFactory(dispatcher, this);
        }
        return commandFactory;
    }

    @Override
    public StateFactory provideStateFactory() {
        if (stateFactory == null) {
            stateFactory = new StateFactory(dispatcher, this);
        }
        return stateFactory;
    }

    @Override
    public void swapState(PresenterState state) {
        this.state = state;
    }
}
