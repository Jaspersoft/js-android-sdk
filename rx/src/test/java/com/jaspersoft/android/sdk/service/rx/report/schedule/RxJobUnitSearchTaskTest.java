package com.jaspersoft.android.sdk.service.rx.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.schedule.JobSearchTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxJobUnitSearchTaskTest {

    @Mock
    JobSearchTask mSyncDelegate;
    @Mock
    ServiceException mServiceException;

    private RxJobSearchTask rxSearchTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxSearchTask = new RxJobSearchTask(mSyncDelegate);
    }

    @Test
    public void should_call_delegate_for_lookup() throws Exception {
        when(mSyncDelegate.nextLookup()).thenReturn(Collections.<JobUnit>emptyList());

        TestSubscriber<List<JobUnit>> test = new TestSubscriber<>();
        rxSearchTask.nextLookup().subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).nextLookup();
    }

    @Test
    public void should_delegate_service_exception_on_nextLookup() throws Exception {
        when(mSyncDelegate.nextLookup()).thenThrow(mServiceException);

        TestSubscriber<List<JobUnit>> test = new TestSubscriber<>();
        rxSearchTask.nextLookup().subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).nextLookup();
    }

    @Test
    public void should_call_delegate_for_hasNext() throws Exception {
        when(mSyncDelegate.hasNext()).thenReturn(true);

        TestSubscriber<Boolean> test = new TestSubscriber<>();
        rxSearchTask.hasNext().subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).hasNext();
    }
}