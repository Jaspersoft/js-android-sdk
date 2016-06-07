package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class HandlerFactory implements CommandHandler.Factory {
    private final Event.Factory eventFactory;

    HandlerFactory() {
        this(new EventFactory());
    }

    HandlerFactory(Event.Factory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public CommandHandler create(Command command) {
        if (command instanceof RunCommand) {
            return new RunCommandHandler(eventFactory);
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
