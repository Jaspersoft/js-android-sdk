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

import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportAttachment;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxReportExportTest {

    @Mock
    ReportExport mSyncDelegate;
    @Mock
    ServiceException mServiceException;

    @Mock
    ReportAttachment mReportAttachment;
    @Mock
    ReportExportOutput mReportExportOutput;


    private RxReportExportImpl rxReportExport;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxReportExport = new RxReportExportImpl(mSyncDelegate);
    }

    @Test
    public void should_execute_download_as_observable() throws Exception {
        when(mSyncDelegate.download()).thenReturn(mReportExportOutput);

        TestSubscriber<ReportExportOutput> test = TestSubscriber.create();
        rxReportExport.download().subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertCompleted();

        verify(mSyncDelegate).download();
    }

    @Test
    public void should_delegate_service_exception_on_download() throws Exception {
        when(mSyncDelegate.download()).thenThrow(mServiceException);

        TestSubscriber<ReportExportOutput> test = TestSubscriber.create();
        rxReportExport.download().subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).download();
    }

    @Test
    public void should_delegate_attachments_call() throws Exception {
        List<ReportAttachment> attachments = Collections.singletonList(mReportAttachment);
        when(mSyncDelegate.getAttachments()).thenReturn(attachments);

        TestSubscriber<List<RxReportAttachment>> test = TestSubscriber.create();
        rxReportExport.getAttachments().subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertCompleted();

        verify(mSyncDelegate).getAttachments();
    }
}