package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.command.rest.RestCommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RestStateFactory extends StateFactory{

    public RestStateFactory(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory) {
        super(dispatcher, eventFactory, commandFactory);
    }

    @Override
    public State createInitedState(State prevState) {
        return new InitedRestState(dispatcher, eventFactory, commandFactory);
    }

    @Override
    public State createRenderedState(State prevState) {
        ReportExecution reportExecution = null;
        if (prevState instanceof  InitedRestState) {
            reportExecution = ((InitedRestState) prevState).reportExecution;
        }
        if (reportExecution != null) {
            return new RenderedRestState(dispatcher, eventFactory, commandFactory, reportExecution);
        }
        throw new RuntimeException("ReportExecution form previous state is absent");
    }
}
