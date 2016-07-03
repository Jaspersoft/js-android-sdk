package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.event.ReportClearedEvent;
import com.jaspersoft.android.sdk.widget.report.event.ReportRenderedEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RenderedVisState extends State {
    RenderedVisState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor) {
        super(dispatcher, eventFactory, commandFactory, commandExecutor);
    }

    @Override
    protected void internalInit(double initialScale) {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRender(RunOptions runOptions) {
        throw new IllegalStateException("Could not render. Already rendered.");
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        setInProgress(true);
        Command applyParamsCommand = commandFactory.createApplyParamsCommand(parameters);
        commandExecutor.execute(applyParamsCommand);
    }

    @Override
    protected void internalNavigateTo(Destination destination) {
        setInProgress(true);
        Command navigateToCommand = commandFactory.createNavigateToCommand(destination);
        commandExecutor.execute(navigateToCommand);
    }

    @Override
    protected void internalRefresh() {
        setInProgress(true);
        Command refreshCommand = commandFactory.createRefreshCommand();
        commandExecutor.execute(refreshCommand);
    }

    @Override
    protected void internalClear() {
        setInProgress(true);
        commandExecutor.cancelExecution();

        Command clearCommand = commandFactory.createClearCommand();
        commandExecutor.execute(clearCommand);
    }

    @Subscribe
    public void onReportRendered(ReportRenderedEvent reportRenderedEvent) {
        setInProgress(false);
    }

    @Subscribe
    public void onReportCleared(ReportClearedEvent reportClearedEvent) {
        setInProgress(false);
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.INITED));
    }

    @Subscribe
    public void onError(ExceptionEvent exceptionEvent) {
        setInProgress(false);
        if (exceptionEvent.getException().code() == StatusCodes.AUTHORIZATION_ERROR) {
            dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.INITED));
        }
    }
}
