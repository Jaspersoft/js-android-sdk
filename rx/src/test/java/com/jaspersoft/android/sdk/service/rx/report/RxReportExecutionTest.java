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

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxReportExecutionTest {
    private static final ReportExportOptions OPTIONS = ReportExportOptions.builder()
            .withFormat(ReportFormat.HTML)
            .build();
    private static final List<ReportParameter> PARAMS = Collections.emptyList();

    @Mock
    ReportExecution mSyncDelegate;
    @Mock
    ServiceException mServiceException;

    @Mock
    ReportExport mReportExport;

    @Rule
    public ExpectedException expected = none();

    private final ReportMetadata fakeReportData = new ReportMetadata("/uri", 100);

    private RxReportExecution rxReportExecution;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxReportExecution = new RxReportExecution(mSyncDelegate);
    }

    @Test
    public void should_execute_export_as_observable() throws Exception {
        when(mSyncDelegate.export(any(ReportExportOptions.class))).thenReturn(mReportExport);

        TestSubscriber<RxReportExport> test = TestSubscriber.create();
        rxReportExecution.export(OPTIONS).subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertCompleted();

        verify(mSyncDelegate).export(OPTIONS);
    }

    @Test
    public void should_not_export_with_null_options() {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Export options should not be null");

        TestSubscriber<RxReportExport> test = TestSubscriber.create();
        rxReportExecution.export(null).subscribe(test);
    }

    @Test
    public void should_delegate_service_exception_on_export() throws Exception {
        when(mSyncDelegate.export(any(ReportExportOptions.class))).thenThrow(mServiceException);

        TestSubscriber<RxReportExport> test = TestSubscriber.create();
        rxReportExecution.export(OPTIONS).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).export(OPTIONS);
    }

    @Test
    public void should_execute_wait_as_observable() throws Exception {
        when(mSyncDelegate.waitForReportCompletion()).thenReturn(fakeReportData);

        TestSubscriber<ReportMetadata> test = TestSubscriber.create();
        rxReportExecution.waitForReportCompletion().subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertCompleted();

        verify(mSyncDelegate).waitForReportCompletion();
    }

    @Test
    public void should_delegate_service_exception_on_wait() throws Exception {
        when(mSyncDelegate.waitForReportCompletion()).thenThrow(mServiceException);

        TestSubscriber<ReportMetadata> test = TestSubscriber.create();
        rxReportExecution.waitForReportCompletion().subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).waitForReportCompletion();
    }

    @Test
    public void should_accept_null_params_for_update() throws Exception {
        TestSubscriber<RxReportExecution> test = TestSubscriber.create();
        rxReportExecution.updateExecution(null).subscribe(test);
        test.assertNoErrors();
    }

    @Test
    public void should_execute_update_as_observable() throws Exception {
        when(mSyncDelegate.updateExecution(anyListOf(ReportParameter.class))).thenReturn(mSyncDelegate);

        TestSubscriber<RxReportExecution> test = TestSubscriber.create();
        rxReportExecution.updateExecution(PARAMS).subscribe(test);

        test.assertNoErrors();
        test.assertValueCount(1);
        test.assertCompleted();

        verify(mSyncDelegate).updateExecution(PARAMS);
    }

    @Test
    public void should_delegate_service_exception_on_update() throws Exception {
        when(mSyncDelegate.updateExecution(anyListOf(ReportParameter.class))).thenThrow(mServiceException);

        TestSubscriber<RxReportExecution> test = TestSubscriber.create();
        rxReportExecution.updateExecution(PARAMS).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).updateExecution(PARAMS);
    }
}