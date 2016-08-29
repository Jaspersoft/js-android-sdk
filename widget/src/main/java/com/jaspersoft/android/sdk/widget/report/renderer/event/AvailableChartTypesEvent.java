package com.jaspersoft.android.sdk.widget.report.renderer.event;

import com.jaspersoft.android.sdk.widget.report.renderer.ChartType;
import java.util.List;

/**
 * @author Olexandr Dahno
 * @since 2.6
 */

public class AvailableChartTypesEvent implements Event {

    private List<ChartType> chartTypes;

    public AvailableChartTypesEvent(List<ChartType> chartTypes) {
        this.chartTypes = chartTypes;
    }

    public List<ChartType> getChartTypes() {
        return chartTypes;
    }
}
