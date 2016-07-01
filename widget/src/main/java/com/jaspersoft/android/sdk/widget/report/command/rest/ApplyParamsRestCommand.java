package com.jaspersoft.android.sdk.widget.report.command.rest;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ApplyParamsRestCommand extends Command<RestEventFactory> {
    private final ReportExecution reportExecution;
    private final List<ReportParameter> parameters;

    ApplyParamsRestCommand(Dispatcher dispatcher, RestEventFactory eventFactory, ReportExecution reportExecution, List<ReportParameter> parameters) {
        super(dispatcher, eventFactory);
        this.reportExecution = reportExecution;
        this.parameters = parameters;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                try {
                    reportExecution.updateExecution(parameters);
                } catch (ServiceException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void voidy) {
                dispatcher.dispatch(eventFactory.createParamsUpdatedEvent());
            }
        };
    }
}
