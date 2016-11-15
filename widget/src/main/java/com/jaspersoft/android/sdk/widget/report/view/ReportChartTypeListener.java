package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.report.renderer.ChartType;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportChartTypeListener {
    void onChartTypesChanged(List<ChartType> chartTypes);

    class SimpleReportChartTypeListener implements ReportChartTypeListener {

        @Override
        public void onChartTypesChanged(List<ChartType> chartTypes) {

        }
    }
}
