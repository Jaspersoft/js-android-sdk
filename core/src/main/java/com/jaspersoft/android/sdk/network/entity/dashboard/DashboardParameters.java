package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardParameters {
    @Expose
    @SerializedName("dashboardParameter")
    private List<ReportParameter> parameters;

    public DashboardParameters(List<ReportParameter> parameters) {
        this.parameters = parameters;
    }

    public List<ReportParameter> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DashboardParameters that = (DashboardParameters) o;

        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;

    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}
