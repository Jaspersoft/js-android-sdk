package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportScheduleRestApi;
import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobForm;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

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
    private final JobSearchCriteriaMapper mSearchCriteriaMapper;
    private final JobDataMapper mJobDataMapper;

    ReportScheduleUseCase(ServiceExceptionMapper exceptionMapper,
                          ReportScheduleRestApi scheduleRestApi,
                          JobSearchCriteriaMapper searchCriteriaMapper,
                          JobDataMapper jobDataMapper) {
        mExceptionMapper = exceptionMapper;
        mScheduleRestApi = scheduleRestApi;
        mSearchCriteriaMapper = searchCriteriaMapper;
        mJobDataMapper = jobDataMapper;
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
            JobDescriptor jobDescriptor = mScheduleRestApi.createJob(form);
            JobData jobData = mJobDataMapper.transform(jobDescriptor);
            return jobData;
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
