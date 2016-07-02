package com.jaspersoft.android.sdk.widget.report.command.rest;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExecutionOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.command.Command;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.event.rest.RestEventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ExecuteReportRestCommand extends Command {
    private final RunOptions runOptions;
    private final ReportService reportService;

    ExecuteReportRestCommand(Dispatcher dispatcher, EventFactory eventFactory, RunOptions runOptions, ReportService reportService) {
        super(dispatcher, eventFactory);
        this.runOptions = runOptions;
        this.reportService = reportService;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, ReportExecution>() {
            @Override
            protected ReportExecution doInBackground(Object... params) {
                try {
                    ReportExecutionOptions reportExecutionOptions = defineRunOptions();
                    return reportService.run(runOptions.getReportUri(), reportExecutionOptions);
                } catch (ServiceException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ReportExecution reportExecution) {
                if (reportExecution != null) {
                    dispatcher.dispatch(eventFactory.createReportExecutedEvent(reportExecution, runOptions.getDestination()));
                }
            }

            private ReportExecutionOptions defineRunOptions() {
                return ReportExecutionOptions.builder()
                        .withFormat(ReportFormat.HTML)
                        .withFreshData(false)
                        .withInteractive(true)
                        .withSaveSnapshot(true)
                        .withParams(runOptions.getParameters())
                        .build();
            }
        };
    }
}
