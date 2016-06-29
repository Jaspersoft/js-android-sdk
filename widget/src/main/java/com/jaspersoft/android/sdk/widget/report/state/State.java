package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

import java.util.List;

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

    protected final void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
        sendProgressChangeEvent();
    }

    public final void init(double initialScale) {
        checkProgressState();
        internalInit(initialScale);
    }

    public final void run(RunOptions runOptions) {
        checkProgressState();
        internalRun(runOptions);
    }

    public final void applyParams(List<ReportParameter> parameters) {
        checkProgressState();
        internalApplyParams(parameters);
    }

    public final void navigateTo(Destination destination) {
        checkProgressState();
        internalNavigateTo(destination);
    }

    protected abstract void internalInit(double initialScale);

    protected abstract void internalRun(RunOptions runOptions);

    protected abstract void internalApplyParams(List<ReportParameter> parameters);

    protected abstract void internalNavigateTo(Destination destination);

    private void checkProgressState() {
        if (inProgress) throw new IllegalStateException("Can not perform action while in progress");
    }

    private void sendProgressChangeEvent() {
        dispatcher.dispatch(eventFactory.createProgressStateEvent(inProgress));
    }
}
