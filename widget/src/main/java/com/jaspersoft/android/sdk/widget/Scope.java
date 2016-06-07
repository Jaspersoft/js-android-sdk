package com.jaspersoft.android.sdk.widget;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class Scope {

    private final CommandHandler.Factory handlerFactory;
    private final ArrayList<CommandHandler> handlers;

    Scope(Bus bus) {
        this(bus, new HandlerFactory());
    }

    Scope(Bus bus, CommandHandler.Factory factory) {
        bus.register(this);
        handlerFactory = factory;
        handlers = new ArrayList<>();
    }

    @Subscribe
    void onRunCommand(RunCommand runCommand) {
        CommandHandler runHandler = handlerFactory.create(runCommand);
        registerHandler(runHandler);
        runHandler.handle();
    }

    private void registerHandler(CommandHandler commandHandler) {
        handlers.add(commandHandler);
    }
}
