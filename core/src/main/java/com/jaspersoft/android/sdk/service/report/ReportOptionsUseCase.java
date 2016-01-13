package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportOptionRestApi;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ReportOptionsUseCase {
    private final ServiceExceptionMapper mExceptionMapper;
    private final ReportOptionRestApi mReportOptionRestApi;

    ReportOptionsUseCase(ServiceExceptionMapper exceptionMapper, ReportOptionRestApi reportOptionRestApi) {
        mExceptionMapper = exceptionMapper;
        mReportOptionRestApi = reportOptionRestApi;
    }

    public Set<ReportOption> requestReportOptionsList(String reportUnitUri) throws ServiceException {
        try {
            return mReportOptionRestApi.requestReportOptionsList(reportUnitUri);
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
            return mReportOptionRestApi.createReportOption(reportUri, optionLabel, parameters, overwrite);
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
