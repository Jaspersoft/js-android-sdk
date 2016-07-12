package com.jaspersoft.android.sdk.widget.report.renderer.command.rest;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class CheckMultiPageRestCommand extends ExportPageRestCommand {
    CheckMultiPageRestCommand(Dispatcher dispatcher, EventFactory eventFactory, ReportExecution reportExecution) {
        super(dispatcher, eventFactory, 2, reportExecution);
    }

    @Override
    protected void onExported(String reportPage) {
        dispatcher.dispatch(eventFactory.createMultiPageStateChangedEvent(true));
    }

    @Override
    protected void onError(ServiceException e) {
        dispatcher.dispatch(eventFactory.createMultiPageStateChangedEvent(false));
    }
}
