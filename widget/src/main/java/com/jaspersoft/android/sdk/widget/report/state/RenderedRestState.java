package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.rest.RestCommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.event.ReportRenderedEvent;
import com.jaspersoft.android.sdk.widget.report.event.rest.PageExportedEvent;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RenderedRestState extends State<RestEventFactory, RestCommandFactory> {
    private final ReportExecution reportExecution;

    RenderedRestState(Dispatcher dispatcher, RestEventFactory eventFactory, RestCommandFactory commandFactory, ReportExecution reportExecution) {
        super(dispatcher, eventFactory, commandFactory);
        this.reportExecution = reportExecution;
    }

    @Override
    protected void internalInit(double initialScale) {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRun(RunOptions runOptions) {
        throw new IllegalStateException("Could not run. Already rendered.");
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        setInProgress(true);
        Command applyParamsCommand = commandFactory.createApplyParamsCommand(parameters, reportExecution);
        applyParamsCommand.execute();
    }

    @Override
    protected void internalNavigateTo(Destination destination) {
        setInProgress(true);
        Command navigateToCommand = commandFactory.createPageExportCommand(destination, reportExecution);
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
    public void onPageExported(PageExportedEvent pageExportedEvent) {
        Command pageExportCommand = commandFactory.createShowPageCommand(pageExportedEvent.getReportPage());
        pageExportCommand.execute();
    }

    @Subscribe
    public void onReportRendered(ReportRenderedEvent reportRenderedEvent) {
        setInProgress(false);
    }
}
