package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportPartsListener {
    void onReportPartsChanged(List<ReportPart> reportPartList);

    void onCurrentReportPartChanged(ReportPart reportPart);

    class SimpleReportPartsListener implements ReportPartsListener{
        @Override
        public void onReportPartsChanged(List<ReportPart> reportPartList) {

        }

        @Override
        public void onCurrentReportPartChanged(ReportPart reportPart) {

        }
    }
}
