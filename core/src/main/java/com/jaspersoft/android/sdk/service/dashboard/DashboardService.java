package com.jaspersoft.android.sdk.service.dashboard;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.DashboardExportRestApi;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecution;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.concurrent.TimeUnit;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardService {
    private final DashboardExportApi dashboardExportApi;

    @TestOnly
    DashboardService(DashboardExportApi dashboardExportApi) {
        this.dashboardExportApi = dashboardExportApi;
    }

    /**
     * Performs request that export dashboard
     *
     * @return dashboard resource output
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public ResourceOutput export(DashboardExportOptions dashboardExportOptions) throws ServiceException {
        if (dashboardExportOptions == null) {
            throw new IllegalArgumentException("Dashboard export options should not be null.");
        }
        DashboardExportExecution exportDetails = dashboardExportApi.start(dashboardExportOptions);
        String exportId = exportDetails.getId();

        dashboardExportApi.awaitReadyStatus(exportId, TimeUnit.SECONDS.toMillis(1));
        return dashboardExportApi.downloadExport(exportId);
    }

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static DashboardService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        DashboardExportRestApi dashboardExportRestApi = client.dashboardExportApi();
        ServiceExceptionMapper serviceExceptionMapper = DefaultExceptionMapper.getInstance();
        DashboardExportOptionsMapper dashboardExportOptionsMapper = new DashboardExportOptionsMapper();
        DashboardExportMapper dashboardExportMapper = new DashboardExportMapper();
        DashboardExportApi dashboardExportApi = new DashboardExportApi(dashboardExportRestApi, serviceExceptionMapper, dashboardExportOptionsMapper, dashboardExportMapper);
        return new DashboardService(dashboardExportApi);
    }
}
