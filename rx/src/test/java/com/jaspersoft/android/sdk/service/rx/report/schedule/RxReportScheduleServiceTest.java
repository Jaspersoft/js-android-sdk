package com.jaspersoft.android.sdk.service.rx.report.schedule;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.schedule.JobData;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.schedule.ReportScheduleService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxReportScheduleServiceTest {

    @Rule
    public ExpectedException expected = none();

    @Mock
    ReportScheduleService mSyncDelegate;
    @Mock
    ServiceException mServiceException;

    @Mock
    AuthorizedClient mAuthorizedClient;

    @Mock
    JobForm mJobForm;
    @Mock
    JobData mJobData;

    private RxReportScheduleService mRxReportScheduleService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mRxReportScheduleService = new RxReportScheduleService(mSyncDelegate);
    }

    @Test
    public void testSearch() throws Exception {
        mRxReportScheduleService.search(null);
    }

    @Test
    public void should_not_accept_null_for_factory_method() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);
        RxReportScheduleService.newService(null);
    }

    @Test
    public void should_create_service_if_client_suplie() throws Exception {
        RxReportScheduleService rxReportScheduleService = RxReportScheduleService.newService(mAuthorizedClient);
        assertThat(rxReportScheduleService, is(notNullValue()));
    }

    @Test
    public void should_call_delegate_for_lookup() throws Exception {
        when(mSyncDelegate.createJob(any(JobForm.class))).thenReturn(mJobData);

        TestSubscriber<JobData> test = createJob();

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).createJob(mJobForm);
    }

    @Test
    public void should_delegate_service_exception_on_nextLookup() throws Exception {
        when(mSyncDelegate.createJob(any(JobForm.class))).thenThrow(mServiceException);

        TestSubscriber<JobData> test = createJob();

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).createJob(mJobForm);
    }

    private TestSubscriber<JobData> createJob() {
        TestSubscriber<JobData> test = new TestSubscriber<>();
        mRxReportScheduleService.createJob(mJobForm).subscribe(test);
        return test;
    }
}