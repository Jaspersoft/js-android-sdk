package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardParameters {
    @Expose
    private List<ReportParameter> parameters;

    public List<ReportParameter> getParameters() {
        return parameters;
    }
}
