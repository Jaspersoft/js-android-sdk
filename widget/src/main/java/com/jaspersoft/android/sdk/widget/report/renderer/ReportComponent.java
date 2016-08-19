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

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public ChartType getChartTypeInstance() {
        if (componentType.equals("chart")) {
            mChartTypeInstance = new ChartType(chartType);
        } else {
            // TODO: implement other cases
        }
        return mChartTypeInstance;
    }

    public void setChartTypeInstance(ChartType chartTypeInstance) {
        this.mChartTypeInstance = chartTypeInstance;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
}
