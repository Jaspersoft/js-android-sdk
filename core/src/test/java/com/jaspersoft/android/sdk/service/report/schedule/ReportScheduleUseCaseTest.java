package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportScheduleRestApi;
import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportScheduleUseCaseTest {
    private static final Map<String, Object> SEARCH_PARAMS = Collections.emptyMap();
    private static final JobSearchCriteria CRITERIA = JobSearchCriteria.empty();

    @Mock
    ServiceExceptionMapper mExceptionMapper;
    @Mock
    JobDataMapper mJobDataMapper;
    @Mock
    ReportScheduleRestApi mScheduleRestApi;
    @Mock
    JobSearchCriteriaMapper mSearchCriteriaMapper;

    @Mock
    JobData mJobData;
    @Mock
    JobForm mJobForm;
    @Mock
    JobDescriptor mJobDescriptor;

    @Mock
    IOException mIOException;
    @Mock
    HttpException mHttpException;
    @Mock
    ServiceException mServiceException;

    private ReportScheduleUseCase useCase;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        setupMocks();
        useCase = new ReportScheduleUseCase(mExceptionMapper, mScheduleRestApi, mSearchCriteriaMapper, mJobDataMapper);
    }

    @Test
    public void should_perform_search() throws Exception {
        useCase.searchJob(CRITERIA);

        verify(mSearchCriteriaMapper).transform(CRITERIA);
        verify(mScheduleRestApi).searchJob(SEARCH_PARAMS);
    }

    @Test
    public void should_perform_create_job() throws Exception {
        when(mScheduleRestApi.createJob(any(JobForm.class))).thenReturn(mJobDescriptor);

        useCase.createJob(mJobForm);

        verify(mJobDataMapper).transform(mJobDescriptor);
        verify(mScheduleRestApi).createJob(mJobForm);
    }

    @Test
    public void search_adapts_io_exception() throws Exception {
        when(mScheduleRestApi.searchJob(anyMapOf(String.class, Object.class))).thenThrow(mIOException);
        try {
            useCase.searchJob(CRITERIA);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void search_adapts_http_exception() throws Exception {
        when(mScheduleRestApi.searchJob(anyMapOf(String.class, Object.class))).thenThrow(mHttpException);

        try {
            useCase.searchJob(CRITERIA);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    @Test
    public void create_job_adapts_io_exception() throws Exception {
        when(mScheduleRestApi.createJob(any(JobForm.class))).thenThrow(mIOException);

        try {
            useCase.createJob(mJobForm);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void create_job_adapts_http_exception() throws Exception {
        when(mScheduleRestApi.createJob(any(JobForm.class))).thenThrow(mHttpException);

        try {
            useCase.createJob(mJobForm);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    private void setupMocks() {
        when(mSearchCriteriaMapper.transform(any(JobSearchCriteria.class)))
                .thenReturn(SEARCH_PARAMS);
        when(mJobDataMapper.transform(any(JobDescriptor.class)))
                .thenReturn(mJobData);
        when(mExceptionMapper.transform(any(HttpException.class))).thenReturn(mServiceException);
        when(mExceptionMapper.transform(any(IOException.class))).thenReturn(mServiceException);
    }
}