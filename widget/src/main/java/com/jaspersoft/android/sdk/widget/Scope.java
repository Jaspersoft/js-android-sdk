package com.jaspersoft.android.sdk.widget;

import com.squareup.otto.Subscribe;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Scope {
    private final CommandHandler.Factory handlerFactory;
    private final Dispatcher dispatcher;
    private final DashboardView view;

    static Scope newInstance(DashboardView view) {
        Dispatcher dispatcher = new Dispatcher();
        HandlerFactory handlerFactory = new HandlerFactory(dispatcher);
        return new Scope(view, dispatcher, handlerFactory);
    }

    Scope(DashboardView view, Dispatcher dispatcher, CommandHandler.Factory factory) {
        this.view = view;

        dispatcher.register(this);
        this.dispatcher = dispatcher;

        handlerFactory = factory;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    @Subscribe
    public void onLoadTemplateCommand(LoadTemplateCommand loadTemplateCommand) {
        CommandHandler<LoadTemplateCommand> initHandler = handlerFactory.loadTemplateCommandHandler();
        initHandler.handle(loadTemplateCommand);
    }

    @Subscribe
    public void onRunCommand(RunCommand runCommand) {
        CommandHandler<RunCommand> runHandler = handlerFactory.runCommandHandler();
        runHandler.handle(runCommand);
    }

    @Subscribe
    public void onEvent(Event event) {
        Event.Type type = event.getType();
        switch (type) {
            case INFLATE_COMPLETE:
                view.lifecycle.onInflateFinish();
                break;
            case SCRIPT_LOADED:
                view.lifecycle.onScriptLoaded();
                break;
        }
    }
}
