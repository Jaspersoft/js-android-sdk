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

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExecutionOptions;
import com.jaspersoft.android.sdk.service.report.ReportService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxReportServiceTest {
    private static final String REPORT_URI = "my/uri";
    private static final ReportExecutionOptions OPTIONS = ReportExecutionOptions.builder().build();

    @Mock
    ReportService mSyncDelegate;
    @Mock
    ReportExecution mReportExecution;
    @Mock
    ServiceException mServiceException;

    @Mock
    AuthorizedClient mAuthorizedClient;

    private RxReportService rxReportService;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxReportService = new RxReportService(mSyncDelegate);
    }

    @Test
    public void should_not_run_with_null_uri() {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");

        TestSubscriber<RxReportExecution> test = TestSubscriber.create();
        rxReportService.run(null, null).subscribe(test);
    }

    @Test
    public void should_run_with_null_options() {
        TestSubscriber<RxReportExecution> test = TestSubscriber.create();
        rxReportService.run(REPORT_URI, null).subscribe(test);
        test.assertNoErrors();
    }

    @Test
    public void should_execute_delegate_as_observable_on_run() throws Exception {
        when(mSyncDelegate.run(anyString(), any(ReportExecutionOptions.class))).thenReturn(mReportExecution);

        TestSubscriber<RxReportExecution> test = TestSubscriber.create();
        rxReportService.run(REPORT_URI, OPTIONS).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).run(REPORT_URI, OPTIONS);
    }

    @Test
    public void should_delegate_service_exception_to_subscription() throws Exception {
        when(mSyncDelegate.run(anyString(), any(ReportExecutionOptions.class))).thenThrow(mServiceException);

        TestSubscriber<RxReportExecution> test = TestSubscriber.create();
        rxReportService.run(REPORT_URI, OPTIONS).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).run(REPORT_URI, OPTIONS);
    }


    @Test
    public void should_provide_impl_with_factory_method() throws Exception {
        RxReportService service = RxReportService.newService(mAuthorizedClient);
        assertThat(service, is(instanceOf(RxReportService.class)));
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void should_not_accept_null_for_factory_method() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);
        RxReportService.newService(null);
    }
}