package com.jaspersoft.android.sdk.service.dashboard;

import com.jaspersoft.android.sdk.network.DashboardExportRestApi;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecution;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionOptions;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class DashboardExportApi {
    private final DashboardExportRestApi dashboardExportRestApi;
    private final ServiceExceptionMapper mExceptionMapper;
    private final DashboardExportOptionsMapper dashboardExportOptionsMapper;
    private final DashboardExportMapper dashboardExportMapper;

    DashboardExportApi(DashboardExportRestApi dashboardExportRestApi, ServiceExceptionMapper mExceptionMapper, DashboardExportOptionsMapper dashboardExportOptionsMapper, DashboardExportMapper dashboardExportMapper) {
        this.dashboardExportRestApi = dashboardExportRestApi;
        this.mExceptionMapper = mExceptionMapper;
        this.dashboardExportOptionsMapper = dashboardExportOptionsMapper;
        this.dashboardExportMapper = dashboardExportMapper;
    }

    public DashboardExportExecution start(DashboardExportOptions dashboardExportOptions) throws ServiceException {
        DashboardExportExecutionOptions options = dashboardExportOptionsMapper.transform(dashboardExportOptions);
        try {
            return dashboardExportRestApi.createExportJob(options);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public void awaitReadyStatus(String exportId, long delay) throws ServiceException {
        DashboardExportStatus status;

        do {
            status = getStatus(exportId);
            if (status == DashboardExportStatus.CANCELLED || status == DashboardExportStatus.FAILED) {
                String message = "Failed to perform dashboard export";
                throw new ServiceException(message, null, StatusCodes.EXPORT_EXECUTION_FAILED);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                throw new ServiceException("Unexpected error", ex, StatusCodes.UNDEFINED_ERROR);
            }
        } while (status != DashboardExportStatus.READY);
    }

    public ResourceOutput downloadExport(String exportId) throws ServiceException {
        try {
            OutputResource result = dashboardExportRestApi.getExportJobResult(exportId);
            return dashboardExportMapper.transform(result);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    private DashboardExportStatus getStatus(String exportId) throws ServiceException {
        try {
            DashboardExportExecutionStatus dashboardExportExecutionStatus = dashboardExportRestApi.getExportJobStatus(exportId);
            return DashboardExportStatus.valueOf(dashboardExportExecutionStatus.getStatus().toUpperCase());
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
