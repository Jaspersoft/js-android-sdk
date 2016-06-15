package com.jaspersoft.android.sdk.widget.report;


import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class CommandHandlerFactory implements CommandHandler.Factory {
    private final Dispatcher dispatcher;
    private final Event.Factory eventFactory;
    private final Command.Factory commandFactory;

    CommandHandlerFactory(Dispatcher dispatcher) {
        this(dispatcher, new EventFactory(), new CommandFactory());
    }

    CommandHandlerFactory(Dispatcher dispatcher, Event.Factory eventFactory, Command.Factory commandFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
    }
}
