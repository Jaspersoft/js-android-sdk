/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

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

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxReportScheduleServiceTest {

    private static final Integer JOB_ID = 1;
    private static final Set<Integer> JOB_IDS = Collections.singleton(JOB_ID);

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
    public void should_call_delegate_for_create_job() throws Exception {
        when(mSyncDelegate.deleteJobs(anySetOf(Integer.class))).thenReturn(JOB_IDS);

        TestSubscriber<Set<Integer>> test = deleteJobs();

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).deleteJobs(JOB_IDS);
    }

    @Test
    public void should_call_delegate_for_delete_jobs() throws Exception {
        when(mSyncDelegate.deleteJobs(anySetOf(Integer.class))).thenReturn(JOB_IDS);

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

    @Test
    public void should_delegate_service_exception_on_update_job() throws Exception {
        when(mSyncDelegate.updateJob(anyInt(), any(JobForm.class))).thenThrow(mServiceException);

        TestSubscriber<JobData> test = updateJob();

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).updateJob(JOB_ID, mJobForm);
    }

    @Test
    public void should_delegate_service_exception_on_delete_jobs() throws Exception {
        when(mSyncDelegate.deleteJobs(anySetOf(Integer.class))).thenThrow(mServiceException);

        TestSubscriber<Set<Integer>> test = deleteJobs();

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).deleteJobs(JOB_IDS);
    }

    @Test
    public void should_delegate_service_exception_on_read_job() throws Exception {
        when(mSyncDelegate.readJob(anyInt())).thenThrow(mServiceException);

        TestSubscriber<JobForm> test = readJob();

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).readJob(JOB_ID);
    }

    private TestSubscriber<Set<Integer>> deleteJobs() {
        TestSubscriber<Set<Integer>> test = new TestSubscriber<>();
        mRxReportScheduleService.deleteJobs(JOB_IDS).subscribe(test);
        return test;
    }

    private TestSubscriber<JobData> createJob() {
        TestSubscriber<JobData> test = new TestSubscriber<>();
        mRxReportScheduleService.createJob(mJobForm).subscribe(test);
        return test;
    }

    private TestSubscriber<JobData> updateJob() {
        TestSubscriber<JobData> test = new TestSubscriber<>();
        mRxReportScheduleService.updateJob(JOB_ID, mJobForm).subscribe(test);
        return test;
    }

    private TestSubscriber<JobForm> readJob() {
        TestSubscriber<JobForm> test = new TestSubscriber<>();
        mRxReportScheduleService.readJob(JOB_ID).subscribe(test);
        return test;
    }
}