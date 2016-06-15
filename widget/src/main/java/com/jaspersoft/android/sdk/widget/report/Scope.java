package com.jaspersoft.android.sdk.widget.report;

import com.jaspersoft.android.sdk.widget.internal.Dispatcher;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Scope {
    private final CommandHandler.Factory handlerFactory;
    private final Dispatcher dispatcher;
    private final ReportClient client;

    private final List<CommandHandler<?>> handlers = new ArrayList<>(10);

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

    @Subscribe
    public void onLoadTemplateCommand(LoadTemplateCommand loadTemplateCommand) {
        CommandHandler<LoadTemplateCommand> initHandler =
                registerHandler(handlerFactory.createProxyLoadTemplateCommandHandler());
        initHandler.handle(loadTemplateCommand);
    }

    @Subscribe
    public void onRunCommand(RunCommand runCommand) {
        CommandHandler<RunCommand> runHandler =
                registerHandler(handlerFactory.runCommandHandler());
        runHandler.handle(runCommand);
    }

    void destroy() {
        for (CommandHandler<?> handler : handlers) {
            handler.cancel();
        }
    }

    Dispatcher getDispatcher() {
        return dispatcher;
    }

    private <C extends Command> CommandHandler<C> registerHandler(CommandHandler<C> handler) {
        handlers.add(handler);
        return handler;
    }
}
