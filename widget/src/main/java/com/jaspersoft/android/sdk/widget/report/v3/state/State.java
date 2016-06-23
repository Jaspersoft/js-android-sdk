package com.jaspersoft.android.sdk.widget.report.v3.state;


import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class State {

    protected final Dispatcher dispatcher;
    protected final EventFactory eventFactory;
    protected final CommandFactory commandFactory;

    private boolean inProgress;

    public State(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
    }

    public final boolean isInProgress() {
        return inProgress;
    }

    protected final void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public final void init() {
        checkProgressState();
        internalInit();
    }

    public final void run(String reportUri) {
        checkProgressState();
        internalRun(reportUri);
    }

    protected abstract void internalInit();

    protected abstract void internalRun(String reportUri);

    private void checkProgressState() {
        if (inProgress) throw new IllegalStateException("Can not perform action while in progress");
    }
}
