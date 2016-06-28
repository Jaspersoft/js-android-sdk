package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.event.ReportRenderedEvent;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RendredState extends State {

    public RendredState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    protected void internalInit(double initialScale) {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRun(String reportUri) {
        setInProgress(true);
        Command runReportCommand = commandFactory.createRunReportCommand(reportUri);
        runReportCommand.execute();
    }

    @Override
    protected void internalApplyParams(List<ReportParameter> parameters) {
        setInProgress(true);
        Command applyParamsCommand = commandFactory.createApplyParamsCommand(parameters);
        applyParamsCommand.execute();
    }

    @Subscribe
    public void onError(ExceptionEvent exceptionEvent) {
        setInProgress(false);
    }

    @Subscribe
    public void onReportRendered(ReportRenderedEvent reportRenderedEvent) {
        setInProgress(false);
    }
}
