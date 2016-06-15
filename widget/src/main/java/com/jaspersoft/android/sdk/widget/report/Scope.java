package com.jaspersoft.android.sdk.widget.report;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Scope {
    private final CommandHandler.Factory handlerFactory;
    private final Dispatcher dispatcher;
    private final ReportClient client;

    static Scope newInstance(ReportClient client) {
        Dispatcher dispatcher = new Dispatcher();
        CommandHandlerFactory commandHandlerFactory = new CommandHandlerFactory(dispatcher);
        return new Scope(client, dispatcher, commandHandlerFactory);
    }

    Scope(ReportClient client, Dispatcher dispatcher, CommandHandler.Factory factory) {
        this.client = client;
        this.handlerFactory = factory;
        this.dispatcher = dispatcher;
        dispatcher.register(this);
    }
}
