package com.jaspersoft.android.sdk.widget.report.event.rest;

import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.event.Event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportExecutedEvent implements Event {
    private final ReportExecution reportExecution;
    private final Destination destination;

    ReportExecutedEvent(ReportExecution reportExecution, Destination destination) {
        this.reportExecution = reportExecution;
        this.destination = destination;
    }

    public ReportExecution getReportExecution() {
        return reportExecution;
    }

    public Destination getDestination() {
        return destination;
    }
}
