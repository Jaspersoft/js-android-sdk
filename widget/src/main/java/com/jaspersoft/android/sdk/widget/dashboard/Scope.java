package com.jaspersoft.android.sdk.widget.dashboard;

import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;
import com.squareup.otto.Subscribe;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class Scope {
    private final CommandHandler.Factory handlerFactory;
    private final Dispatcher dispatcher;
    private final DashboardClient view;

    static Scope newInstance(DashboardClient view) {
        Dispatcher dispatcher = new Dispatcher();
        CommandHandlerFactory commandHandlerFactory = new CommandHandlerFactory(dispatcher);
        return new Scope(view, dispatcher, commandHandlerFactory);
    }

    Scope(DashboardClient view, Dispatcher dispatcher, CommandHandler.Factory factory) {
        this.view = view;
        this.handlerFactory = factory;
        this.dispatcher = dispatcher;
        dispatcher.register(this);
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
    public void onMinimizeCommand(MinimizeCommand minimizeCommand) {
        CommandHandler<MinimizeCommand> runHandler = handlerFactory.minimizeCommandHandler();
        runHandler.handle(minimizeCommand);
    }

    @Subscribe
    public void onEvent(Event event) {
        Event.Type type = event.getType();
        switch (type) {
            case INFLATE_COMPLETE:
                view.lifecycleCallbacks.onInflateFinish();
                break;
            case SCRIPT_LOADED:
                view.lifecycleCallbacks.onScriptLoaded();
                break;
            case DASHBOARD_LOADED:
                view.lifecycleCallbacks.onDashboardRendered();
                break;
            case WINDOW_ERROR:
                view.errorCallbacks.onWindowError(event.firstArg(WindowError.class));
                break;
            case MAXIMIZE_START:
                view.dashletCallbacks.onMaximizeStart(event.firstArg(String.class));
                break;
            case MAXIMIZE_END:
                view.dashletCallbacks.onMaximizeEnd(event.firstArg(String.class));
                break;
            case MINIMIZE_START:
                view.dashletCallbacks.onMinimizeStart(event.firstArg(String.class));
                break;
            case MINIMIZE_END:
                view.dashletCallbacks.onMinimizeEnd(event.firstArg(String.class));
                break;
            case HYPERLINK_CLICK:
                view.dashletCallbacks.onHypeLinkClick(event.firstArg(DashboardHyperlink.class));
                break;
        }
    }
}
