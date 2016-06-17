package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
abstract class PresenterState {
    private final CommandRegistry commandRegistry;
    private final Dispatcher dispatcher;
    private final StateContext stateContext;

    protected PresenterState(StateContext stateContext, Dispatcher dispatcher) {
        this(new CommandRegistry(), dispatcher, stateContext);
    }

    protected PresenterState(CommandRegistry commandRegistry, Dispatcher dispatcher, StateContext stateContext) {
        this.commandRegistry = commandRegistry;
        this.stateContext = stateContext;
        this.dispatcher = dispatcher;
        this.dispatcher.register(this);
    }

    public abstract void run(RunOptions options);

    public abstract void update(List<ReportParameter> parameters);

    public abstract boolean isRunning();

    public abstract void navigate(ReportQuery query);

    public abstract void refresh();

    public final void resume() {
        dispatcher.register(this);
    }

    public final void pause() {
        dispatcher.unregister(this);
    }

    public final void destroy() {
        commandRegistry.cancelAll();
    }

    StateContext getStateContext() {
        return stateContext;
    }

    final void executeCommand(Command command) {
        commandRegistry.register(command);
        command.execute();
    }

    protected interface StateContext {
        WebView getWebView();

        String getUri();

        AuthorizedClient getClient();

        CommandFactory getCommandFactory();

        StateFactory getStateFactory();

        void swapState(PresenterState state);
    }
}
