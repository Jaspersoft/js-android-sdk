package com.jaspersoft.android.sdk.widget.report.v3.state;


import com.jaspersoft.android.sdk.widget.report.v3.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.v3.RenderState;
import com.jaspersoft.android.sdk.widget.report.v3.command.Command;
import com.jaspersoft.android.sdk.widget.report.v3.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.v3.event.ExceptionEvent;
import com.jaspersoft.android.sdk.widget.report.v3.event.ReportRenderedEvent;
import com.squareup.otto.Subscribe;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InitedState extends State {
    public InitedState(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    protected void internalInit() {
        throw new IllegalStateException("Could not init. Already inited.");
    }

    @Override
    protected void internalRun(String reportUri) {
        setInProgress(true);
        Command runReportCommand = commandFactory.createRunReportCommand(reportUri);
        runReportCommand.execute();
    }

    @Subscribe
    public void onError(ExceptionEvent exceptionEvent) {
        setInProgress(false);
    }

    @Subscribe
    public void onReportRendered(ReportRenderedEvent reportRenderedEvent) {
        setInProgress(false);
        dispatcher.dispatch(eventFactory.createSwapStateEvent(RenderState.RENDERED));
    }
}
