package com.jaspersoft.android.sdk.widget.report.renderer.event.rest;

import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.ErrorMapper;
import com.jaspersoft.android.sdk.widget.report.renderer.event.Event;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RestEventFactory extends EventFactory {
    public RestEventFactory(ErrorMapper errorMapper) {
        super(errorMapper);
    }

    @Override
    public Event createHyperlinkEvent(Hyperlink hyperlink) {
        throw new UnsupportedOperationException("Hyperlinks event is not supported by rest");
    }

    @Override
    public Event createReportExecutedEvent(ReportExecution reportExecution, Destination destination) {
        return new ReportExecutedEvent(reportExecution, destination);
    }

    @Override
    public Event createPageExportedEvent(String reportPage, int pageNumber) {
        return new PageExportedEvent(reportPage, pageNumber);
    }

    @Override
    public Event createParamsUpdatedEvent() {
        return new ParamsUpdatedEvent();
    }

    @Override
    public Event createDaraRefreshedEvent() {
        return new DataRefreshedEvent();
    }
}
