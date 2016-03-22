package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class RepositoryUseCase {
    private final ServiceExceptionMapper mExceptionMapper;
    private final RepositoryRestApi mRestApi;

    public RepositoryUseCase(ServiceExceptionMapper exceptionMapper, RepositoryRestApi restApi) {
        mExceptionMapper = exceptionMapper;
        mRestApi = restApi;
    }

    public DashboardComponentCollection requestDashboardComponents(String dashboardUri) throws ServiceException {
        try {
            return mRestApi.requestDashboardComponents(dashboardUri);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
