package com.jaspersoft.android.sdk.widget.report.event.rest;

import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.event.Event;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RestEventFactory extends EventFactory {
    public RestEventFactory(ErrorMapper errorMapper) {
        super(errorMapper);
    }

    public Event createReportExecutedEvent(ReportExecution reportExecution, Destination destination) {
        return new ReportExecutedEvent(reportExecution, destination);
    }

    public Event createPageExportedEvent(String reportPage) {
        return new PageExportedEvent(reportPage);
    }

    public Event createParamsUpdatedEvent() {
        return new ParamsUpdatedEvent();
    }
}
