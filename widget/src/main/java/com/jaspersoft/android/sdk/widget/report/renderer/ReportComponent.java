package com.jaspersoft.android.sdk.widget.report.renderer;

/**
 * @author Olexandr Dahno
 * @since 2.6
 */

public class ReportComponent {
    private String id;
    private String componentType;
    // TODO: move into child class (ChartReportComponent)
    private ChartType mChartTypeInstance;
    private String chartType;

    public String getId() {
        return id;
    }

    public String getComponentType() {
        return componentType;
    }

    public ChartType getChartTypeInstance() {
        if (componentType.equals("chart")) {
            mChartTypeInstance = new ChartType(chartType);
        } else {
            // TODO: implement other cases
        }
        return mChartTypeInstance;
    }
}
