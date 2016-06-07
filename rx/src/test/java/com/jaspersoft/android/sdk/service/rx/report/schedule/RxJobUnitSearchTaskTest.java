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

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
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
        rxSearchTask.hasNext();
        verify(mSyncDelegate).hasNext();
    }
}