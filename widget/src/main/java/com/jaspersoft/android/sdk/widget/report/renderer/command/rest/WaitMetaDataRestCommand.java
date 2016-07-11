package com.jaspersoft.android.sdk.widget.report.renderer.command.rest;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.command.Command;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class WaitMetaDataRestCommand extends Command {
    private final ReportExecution reportExecution;

    WaitMetaDataRestCommand(Dispatcher dispatcher, EventFactory eventFactory, ReportExecution reportExecution) {
        super(dispatcher, eventFactory);
        this.reportExecution = reportExecution;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, ReportMetadata>() {
            @Override
            protected ReportMetadata doInBackground(Object... params) {
                try {
                    return reportExecution.waitForReportCompletion();
                } catch (ServiceException e) {
                    dispatcher.dispatch(eventFactory.createErrorEvent(e));
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ReportMetadata reportMetadata) {
                if (reportMetadata != null) {
                    dispatcher.dispatch(eventFactory.createPagesCountChangedEvent(reportMetadata.getTotalPages()));
                }
            }
        };
    }
}
