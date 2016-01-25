package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportScheduleRestApi;
import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ReportScheduleUseCase {
    private final ServiceExceptionMapper mExceptionMapper;
    private final ReportScheduleRestApi mScheduleRestApi;
    private final InfoCacheManager mCacheManager;
    private final JobSearchCriteriaMapper mSearchCriteriaMapper;
    private final JobDataMapper mJobDataMapper;
    private final JobFormMapper mJobFormMapper;

    ReportScheduleUseCase(ServiceExceptionMapper exceptionMapper,
                          ReportScheduleRestApi scheduleRestApi,
                          InfoCacheManager cacheManager,
                          JobSearchCriteriaMapper searchCriteriaMapper,
                          JobDataMapper jobDataMapper,
                          JobFormMapper jobFormMapper
    ) {
        mExceptionMapper = exceptionMapper;
        mScheduleRestApi = scheduleRestApi;
        mCacheManager = cacheManager;
        mSearchCriteriaMapper = searchCriteriaMapper;
        mJobDataMapper = jobDataMapper;
        mJobFormMapper = jobFormMapper;
    }

    public List<JobUnit> searchJob(JobSearchCriteria criteria) throws ServiceException {
        try {
            Map<String, Object> searchParams = mSearchCriteriaMapper.transform(criteria);
            return mScheduleRestApi.searchJob(searchParams);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public JobData createJob(JobForm form) throws ServiceException {
        try {
            ServerInfo info = mCacheManager.getInfo();
            JobFormEntity formEntity = mJobFormMapper.transform(form);
            JobDescriptor jobDescriptor = mScheduleRestApi.createJob(formEntity);
            return mJobDataMapper.transform(jobDescriptor, info.getDatetimeFormatPattern());
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
