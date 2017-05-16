package com.jaspersoft.android.sdk.widget.report.renderer.event.rest;

import com.jaspersoft.android.sdk.widget.report.renderer.event.Event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class PageExportedEvent implements Event {
    private final String reportPage;
    private final int pageNumber;

    PageExportedEvent(String reportPage, int pageNumber) {
        this.reportPage = reportPage;
        this.pageNumber = pageNumber;
    }

    public String getReportPage() {
        return reportPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
