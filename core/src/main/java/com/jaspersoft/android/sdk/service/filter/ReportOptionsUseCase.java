package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportOptionRestApi;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportOptionsUseCase {
    private final ServiceExceptionMapper mExceptionMapper;
    private final ReportOptionRestApi mReportOptionRestApi;
    private final ReportOptionMapper mReportOptionMapper;

    ReportOptionsUseCase(ServiceExceptionMapper exceptionMapper, ReportOptionRestApi reportOptionRestApi, ReportOptionMapper reportOptionMapper) {
        mExceptionMapper = exceptionMapper;
        mReportOptionRestApi = reportOptionRestApi;
        mReportOptionMapper = reportOptionMapper;
    }

    public Set<ReportOption> requestReportOptionsList(String reportUnitUri) throws ServiceException {
        try {
            Set<ReportOptionEntity> entities = mReportOptionRestApi.requestReportOptionsList(reportUnitUri);
            return mReportOptionMapper.transform(entities);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public ReportOption createReportOption(String reportUri,
                                           String optionLabel,
                                           List<ReportParameter> parameters,
                                           boolean overwrite) throws ServiceException {
        try {
            ReportOptionEntity entity = mReportOptionRestApi.createReportOption(reportUri, optionLabel, parameters, overwrite);
            return mReportOptionMapper.transform(entity);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public void updateReportOption(String reportUri, String optionId, List<ReportParameter> parameters) throws ServiceException {
        try {
            mReportOptionRestApi.updateReportOption(reportUri, optionId, parameters);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public void deleteReportOption(String reportUri, String optionId) throws ServiceException {
        try {
            mReportOptionRestApi.deleteReportOption(reportUri, optionId);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
