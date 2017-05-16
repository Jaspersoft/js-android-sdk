package com.jaspersoft.android.sdk.service.dashboard;

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionOptions;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardParameters;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class DashboardExportOptionsMapper {
    public DashboardExportExecutionOptions transform(DashboardExportOptions dashboardExportOptions) {
        String format = dashboardExportOptions.getFormat().name().toLowerCase();
        DashboardParameters dashboardParameters = new DashboardParameters(dashboardExportOptions.getParameters());
        return new DashboardExportExecutionOptions(dashboardExportOptions.getUri(), dashboardExportOptions.getWidth(),
                dashboardExportOptions.getHeight(), format, dashboardParameters);
    }
}
