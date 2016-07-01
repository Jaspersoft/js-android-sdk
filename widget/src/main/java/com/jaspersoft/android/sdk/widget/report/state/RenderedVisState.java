package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.vis.VisCommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.event.ReportRenderedEvent;
import com.jaspersoft.android.sdk.widget.report.event.vis.VisEventFactory;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RenderedVisState extends State<VisEventFactory, VisCommandFactory> {

    RenderedVisState(Dispatcher dispatcher, VisEventFactory eventFactory, VisCommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    protected void internalInit(double initialScale) {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRun(RunOptions runOptions) {
        setInProgress(true);
        Command runReportCommand = commandFactory.createRunReportCommand(runOptions);
        runReportCommand.execute();
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        setInProgress(true);
        Command applyParamsCommand = commandFactory.createApplyParamsCommand(parameters);
        applyParamsCommand.execute();
    }

    @Override
    protected void internalNavigateTo(Destination destination) {
        setInProgress(true);
        Command navigateToCommand = commandFactory.createNavigateToCommand(destination);
        navigateToCommand.execute();
    }

    @Subscribe
    public void onError(ExceptionEvent exceptionEvent) {
        setInProgress(false);
        if (exceptionEvent.getException().code() == StatusCodes.AUTHORIZATION_ERROR) {
            dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.INITED));
        }
    }

    @Subscribe
    public void onReportRendered(ReportRenderedEvent reportRenderedEvent) {
        setInProgress(false);
    }
}
