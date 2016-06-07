package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class HandlerFactory implements CommandHandler.Factory {
    private final Dispatcher dispatcher;
    private final Event.Factory eventFactory;
    private final Command.Factory commandFactory;

    HandlerFactory(Dispatcher dispatcher) {
        this(dispatcher, new EventFactory(), new CommandFactory());
    }

    HandlerFactory(Dispatcher dispatcher, Event.Factory eventFactory, Command.Factory commandFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
    }

    @Override
    public CommandHandler loadTemplateCommandHandler() {
        return new LoadTemplateCommandHandler(dispatcher, eventFactory, commandFactory);
    }

    @Override
    public CommandHandler<RunCommand> runCommandHandler() {
        return new RunCommandHandler();
    }
}
