package com.jaspersoft.android.sdk.widget.report.state;


import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.CommandExecutor;
import com.jaspersoft.android.sdk.widget.report.command.CommandFactory;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RestStateFactory extends StateFactory{
    public RestStateFactory(Dispatcher dispatcher, EventFactory eventFactory, CommandFactory commandFactory, CommandExecutor commandExecutor) {
        super(dispatcher, eventFactory, commandFactory, commandExecutor);
    }

    @Override
    public State createInitedState(State prevState) {
        return new InitedRestState(dispatcher, eventFactory, commandFactory, commandExecutor);
    }

    @Override
    public State createRenderedState(State prevState) {
        ReportExecution reportExecution = null;
        if (prevState instanceof  InitedRestState) {
            reportExecution = ((InitedRestState) prevState).reportExecution;
        }
        if (reportExecution != null) {
            return new RenderedRestState(dispatcher, eventFactory, commandFactory, commandExecutor, reportExecution);
        }
        throw new RuntimeException("ReportExecution form previous state is absent");
    }
}
