package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
abstract class PresenterState {
    private final CommandRegistry commandRegistry;
    private final Dispatcher dispatcher;
    private final Context context;

    protected PresenterState(Context context, Dispatcher dispatcher) {
        this(context, dispatcher, new CommandRegistry());
    }

    protected PresenterState(Context context, Dispatcher dispatcher, CommandRegistry commandRegistry) {
        this.context = context;
        this.dispatcher = dispatcher;
        this.commandRegistry = commandRegistry;
        this.dispatcher.register(this);
    }

    @Subscribe
    public void onSdkError(ServiceException serviceException) {
        context.getListeners().getErrorListener().onSdkError(serviceException);
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

    Context getContext() {
        return context;
    }

    final void executeCommand(Command command) {
        commandRegistry.register(command);
        command.execute();
    }

    final void swapState(PresenterState nextState) {
        context.swapState(nextState);
    }

    final void dispatchProgress(int newProgress) {
        getContext().getListeners().getProgressListener().onProgressChanged(newProgress);
    }

    final void dispatchWindowError(WindowError error) {
        getContext().getListeners().getErrorListener().onWebWindowError(error);
    }

    protected interface Context {
        String getUri();

        WebView getWebView();

        PresenterListeners getListeners();

        AuthorizedClient getClient();

        PresenterState getCurrentState();

        CommandFactory provideCommandFactory();

        StateFactory provideStateFactory();

        void swapState(PresenterState state);
    }
}
