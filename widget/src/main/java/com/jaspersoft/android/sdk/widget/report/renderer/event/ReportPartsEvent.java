package com.jaspersoft.android.sdk.widget.report.renderer.event;

import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportPartsEvent implements Event {
    private final List<ReportPart> reportPartList;

    public ReportPartsEvent(List<ReportPart> reportPartList) {
        this.reportPartList = reportPartList;
    }

    public List<ReportPart> getReportPartList() {
        return reportPartList;
    }
}
