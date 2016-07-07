package com.jaspersoft.android.sdk.widget.report.renderer.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportAction;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.renderer.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class State {
    protected final Dispatcher dispatcher;
    protected final EventFactory eventFactory;
    protected final CommandFactory commandFactory;
    protected final CommandExecutor commandExecutor;

    private boolean inProgress;

    State(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
        this.commandExecutor = commandExecutor;
    }

    public final boolean isActionAvailable(ReportAction reportAction) {
        if (reportAction == ReportAction.DESTROY) return internalIsActionAvailable(reportAction);
        else return !inProgress && internalIsActionAvailable(reportAction);
    }

    public boolean isInProgress() {
        return inProgress;
    }

    protected final void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
        sendProgressChangeEvent();
    }

    public final void init(double initialScale) {
        checkProgressState();
        internalInit(initialScale);
    }

    public final void render(RunOptions runOptions) {
        checkProgressState();
        internalRender(runOptions);
    }

    public final void applyParams(List<ReportParameter> parameters) {
        checkProgressState();
        internalApplyParams(parameters);
    }

    public final void navigateTo(Destination destination) {
        checkProgressState();
        internalNavigateTo(destination);
    }

    public final void refresh() {
        checkProgressState();
        internalRefresh();
    }

    public final void reset() {
        internalReset();
    }

    public final void destroy() {
        internalDestroy();
    }

    public abstract RenderState getName();

    public abstract boolean internalIsActionAvailable(ReportAction reportAction);

    protected abstract void internalInit(double initialScale);

    protected abstract void internalRender(RunOptions runOptions);

    protected abstract void internalApplyParams(List<ReportParameter> parameters);

    protected abstract void internalNavigateTo(Destination destination);

    protected abstract void internalRefresh();

    protected abstract void internalReset();

    protected void internalDestroy(){
        reset();
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.DESTROYED));
    }

    private void checkProgressState() {
        if (inProgress) throw new IllegalStateException("Can not perform action while other is in progress");
    }

    private void sendProgressChangeEvent() {
        dispatcher.dispatch(eventFactory.createProgressStateEvent(inProgress));
    }
}
