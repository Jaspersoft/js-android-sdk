package com.jaspersoft.android.sdk.widget.report;

import android.util.Log;

import com.jaspersoft.android.sdk.widget.WindowError;
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
        Log.d(Dispatcher.LOG_TAG, "<=========== Handled - " + loadTemplateCommand);
    }

    @Subscribe
    public void onRunCommand(RunCommand runCommand) {
        CommandHandler<RunCommand> runHandler =
                registerHandler(handlerFactory.createRunCommandHandler(runCommand.getVersion()));
        runHandler.handle(runCommand);
        Log.d(Dispatcher.LOG_TAG, "<=========== Handled - " + runCommand);
    }

    @Subscribe
    public void onEvent(Event event) {
        Event.Type type = event.getType();
        switch (type) {
            case INFLATE_COMPLETE:
                client.lifecycleCallbacks.onInflateFinish();
                break;
            case SCRIPT_LOADED:
                client.lifecycleCallbacks.onScriptLoaded();
                break;
            case REPORT_LOADED:
                client.lifecycleCallbacks.onReportRendered();
                break;
            case WINDOW_ERROR:
                client.errorCallbacks.onWindowError(event.firstArg(WindowError.class));
                break;
        }
        Log.d(Dispatcher.LOG_TAG, "<=========== Handled - " + event);
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
