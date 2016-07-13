package com.jaspersoft.android.sdk.widget.report.renderer.command.rest;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class RefreshRestCommand extends Command {
    private final ReportExecution reportExecution;

    RefreshRestCommand(Dispatcher dispatcher, EventFactory eventFactory, ReportExecution reportExecution) {
        super(dispatcher, eventFactory);
        this.reportExecution = reportExecution;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, Void>() {
            @Override
            protected void onPreExecute() {
                dispatcher.dispatch(eventFactory.createMultiPageStateChangedEvent(false));
                dispatcher.dispatch(eventFactory.createPagesCountChangedEvent(null));
            }

            @Override
            protected Void doInBackground(Object... params) {
                try {
                    reportExecution.refresh();
                } catch (ServiceException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void voidy) {
                dispatcher.dispatch(eventFactory.createDaraRefreshedEvent());
            }
        };
    }
}
