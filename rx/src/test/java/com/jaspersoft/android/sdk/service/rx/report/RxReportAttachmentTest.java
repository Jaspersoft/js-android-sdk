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

package com.jaspersoft.android.sdk.service.rx.report;

import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportAttachment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxReportAttachmentTest {

    @Mock
    ReportAttachment mSyncDelegate;
    @Mock
    ResourceOutput mResourceOutput;
    @Mock
    ServiceException mServiceException;

    private RxReportAttachment rxReportAttachment;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxReportAttachment = new RxReportAttachment(mSyncDelegate);
    }

    @Test
    public void should_execute_download_as_observable() throws Exception {
        when(mSyncDelegate.download()).thenReturn(mResourceOutput);

        TestSubscriber<ResourceOutput> test = TestSubscriber.create();
        rxReportAttachment.download().subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertCompleted();

        verify(mSyncDelegate).download();
    }

    @Test
    public void should_delegate_service_exception_on_download() throws Exception {
        when(mSyncDelegate.download()).thenThrow(mServiceException);

        TestSubscriber<ResourceOutput> test = TestSubscriber.create();
        rxReportAttachment.download().subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).download();
    }
}