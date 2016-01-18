/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.rx.repository;

import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.repository.RepositorySearchTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxRepositorySearchTaskTest {

    @Mock
    RepositorySearchTask mSyncDelegate;
    @Mock
    ServiceException mServiceException;

    private RxRepositorySearchTask mRxRepositorySearchTask;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mRxRepositorySearchTask = new RxRepositorySearchTask(mSyncDelegate);
    }

    @Test
    public void should_call_delegate_for_lookup() throws Exception {
        when(mSyncDelegate.nextLookup()).thenReturn(Collections.<Resource>emptyList());

        TestSubscriber<List<Resource>> test = new TestSubscriber<>();
        mRxRepositorySearchTask.nextLookup().subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).nextLookup();
    }

    @Test
    public void should_delegate_service_exception_on_nextLookup() throws Exception {
        when(mSyncDelegate.nextLookup()).thenThrow(mServiceException);

        TestSubscriber<List<Resource>> test = new TestSubscriber<>();
        mRxRepositorySearchTask.nextLookup().subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).nextLookup();
    }

    @Test
    public void should_call_delegate_for_hasNext() throws Exception {
        when(mSyncDelegate.hasNext()).thenReturn(true);

        TestSubscriber<Boolean> test = new TestSubscriber<>();
        mRxRepositorySearchTask.hasNext().subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).hasNext();
    }
}