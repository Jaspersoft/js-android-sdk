package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportScheduleRestApi;
import com.jaspersoft.android.sdk.network.entity.schedule.JobDescriptor;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportScheduleUseCaseTest {
    private static final Map<String, Object> SEARCH_PARAMS = Collections.emptyMap();
    private static final JobSearchCriteria CRITERIA = JobSearchCriteria.empty();
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat();
    private static final Set<Integer> JOB_IDS = Collections.singleton(1);
    private static final Integer JOB_ID = 1;

    @Mock
    ServiceExceptionMapper mExceptionMapper;
    @Mock
    JobDataMapper mJobDataMapper;
    @Mock
    JobFormMapper mJobFormMapper;
    @Mock
    JobUnitMapper mJobUnitMapper;
    @Mock
    ReportScheduleRestApi mScheduleRestApi;
    @Mock
    JobSearchCriteriaMapper mSearchCriteriaMapper;

    @Mock
    InfoCacheManager mInfoCacheManager;
    @Mock
    ServerInfo mServerInfo;

    @Mock
    JobData mJobData;
    @Mock
    JobFormEntity mJobFormEntity;
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
    @Mock
    JobUnit mJobUnit;

    private ReportScheduleUseCase useCase;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        setupMocks();
        useCase = new ReportScheduleUseCase(
                mExceptionMapper,
                mScheduleRestApi,
                mInfoCacheManager,
                mSearchCriteriaMapper,
                mJobDataMapper,
                mJobFormMapper,
                mJobUnitMapper
        );
    }

    @Test
    public void should_perform_search() throws Exception {
        when(mScheduleRestApi.searchJob(anyMapOf(String.class, Object.class))).thenReturn(Collections.<JobUnitEntity>emptyList());
        useCase.searchJob(CRITERIA);

        verify(mSearchCriteriaMapper).transform(CRITERIA);
        verify(mScheduleRestApi).searchJob(SEARCH_PARAMS);
        verify(mJobUnitMapper).transform(Collections.<JobUnitEntity>emptyList());
    }

    @Test
    public void should_perform_create_job() throws Exception {
        when(mScheduleRestApi.createJob(any(JobFormEntity.class))).thenReturn(mJobDescriptor);

        useCase.createJob(mJobForm);

        verify(mJobDataMapper).transform(mJobDescriptor, SIMPLE_DATE_FORMAT);
        verify(mScheduleRestApi).createJob(mJobFormEntity);
    }

    @Test
    public void should_perform_update_job() throws Exception {
        when(mScheduleRestApi.updateJob(anyInt(), any(JobFormEntity.class))).thenReturn(mJobDescriptor);

        useCase.updateJob(JOB_ID, mJobForm);

        verify(mJobDataMapper).transform(mJobDescriptor, SIMPLE_DATE_FORMAT);
        verify(mScheduleRestApi).updateJob(JOB_ID, mJobFormEntity);
    }

    @Test
    public void should_perform_delete_jobs() throws Exception {
        useCase.deleteJobs(JOB_IDS);
        verify(mScheduleRestApi).deleteJobs(JOB_IDS);
    }

    @Test
    public void should_perform_read_job() throws Exception {
        when(mScheduleRestApi.requestJob(anyInt())).thenReturn(mJobFormEntity);

        JobForm expected = useCase.readJob(JOB_ID);
        assertThat(mJobForm, is(expected));

        verify(mJobFormMapper).toDataForm(mJobFormEntity);
        verify(mScheduleRestApi).requestJob(JOB_ID);
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
    public void delete_jobs_adapts_io_exception() throws Exception {
        when(mScheduleRestApi.deleteJobs(anySetOf(Integer.class))).thenThrow(mIOException);

        try {
            useCase.deleteJobs(JOB_IDS);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void delete_jobs_adapts_http_exception() throws Exception {
        when(mScheduleRestApi.deleteJobs(anySetOf(Integer.class))).thenThrow(mHttpException);

        try {
            useCase.deleteJobs(JOB_IDS);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    @Test
    public void create_job_adapts_io_exception() throws Exception {
        when(mScheduleRestApi.createJob(any(JobFormEntity.class))).thenThrow(mIOException);

        try {
            useCase.createJob(mJobForm);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void create_job_adapts_http_exception() throws Exception {
        when(mScheduleRestApi.createJob(any(JobFormEntity.class))).thenThrow(mHttpException);

        try {
            useCase.createJob(mJobForm);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    @Test
    public void read_job_adapts_io_exception() throws Exception {
        when(mScheduleRestApi.requestJob(anyInt())).thenThrow(mIOException);

        try {
            useCase.readJob(JOB_ID);
            fail("Should adapt IO exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mIOException);
        }
    }

    @Test
    public void read_job_adapts_http_exception() throws Exception {
        when(mScheduleRestApi.requestJob(anyInt())).thenThrow(mHttpException);

        try {
            useCase.readJob(JOB_ID);
            fail("Should adapt HTTP exception");
        } catch (ServiceException ex) {
            verify(mExceptionMapper).transform(mHttpException);
        }
    }

    private void setupMocks() throws Exception {
        when(mInfoCacheManager.getInfo()).thenReturn(mServerInfo);
        when(mServerInfo.getDatetimeFormatPattern()).thenReturn(SIMPLE_DATE_FORMAT);

        when(mSearchCriteriaMapper.transform(any(JobSearchCriteria.class)))
                .thenReturn(SEARCH_PARAMS);
        when(mJobDataMapper.transform(any(JobDescriptor.class), any(SimpleDateFormat.class)))
                .thenReturn(mJobData);
        when(mJobFormMapper.toFormEntity(any(JobForm.class)))
                .thenReturn(mJobFormEntity);
        when(mJobFormMapper.toDataForm(any(JobFormEntity.class)))
                .thenReturn(mJobForm);
        when(mJobUnitMapper.transform(anyListOf(JobUnitEntity.class)))
                .thenReturn(Collections.singletonList(mJobUnit));

        when(mExceptionMapper.transform(any(HttpException.class))).thenReturn(mServiceException);
        when(mExceptionMapper.transform(any(IOException.class))).thenReturn(mServiceException);
    }
}