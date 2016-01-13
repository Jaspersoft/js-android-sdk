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
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxReportServiceTest {
    private static final String REPORT_URI = "my/uri";
    private static final ReportExecutionOptions OPTIONS = ReportExecutionOptions.builder().build();
    private static final List<ReportParameter> PARAMS = Collections.emptyList();
    private static final String OPTION_LABEL = "label";
    private static final String OPTION_ID = OPTION_LABEL;

    @Mock
    ReportService mSyncDelegate;
    @Mock
    ReportExecution mReportExecution;
    @Mock
    ServiceException mServiceException;

    private ReportOption fakeReportOption = new ReportOption();

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
    public void should_delegate_service_exception_to_subscription_on_list_controls() throws Exception {
        when(mSyncDelegate.listControls(anyString())).thenThrow(mServiceException);

        TestSubscriber<List<InputControl>> test = TestSubscriber.create();
        rxReportService.listControls(REPORT_URI).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).listControls(REPORT_URI);
    }

    @Test
    public void should_execute_delegate_as_observable_on_list_controls() throws Exception {
        when(mSyncDelegate.listControls(anyString())).thenReturn(Collections.<InputControl>emptyList());

        TestSubscriber<List<InputControl>> test = TestSubscriber.create();
        rxReportService.listControls(REPORT_URI).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).listControls(REPORT_URI);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_list_controls_values() throws Exception {
        when(mSyncDelegate.listControlsValues(anyString(), anyListOf(ReportParameter.class))).thenThrow(mServiceException);

        TestSubscriber<List<InputControlState>> test = TestSubscriber.create();
        rxReportService.listControlsValues(REPORT_URI, PARAMS).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).listControlsValues(REPORT_URI, PARAMS);
    }

    @Test
    public void should_execute_delegate_as_observable_on_list_controls_values() throws Exception {
        when(mSyncDelegate.listControlsValues(anyString(), anyListOf(ReportParameter.class)))
                .thenReturn(Collections.<InputControlState>emptyList());

        TestSubscriber<List<InputControlState>> test = TestSubscriber.create();
        rxReportService.listControlsValues(REPORT_URI, PARAMS).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).listControlsValues(REPORT_URI, PARAMS);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_list_report_options() throws Exception {
        when(mSyncDelegate.listReportOptions(anyString())).thenThrow(mServiceException);

        TestSubscriber<Set<ReportOption>> test = TestSubscriber.create();
        rxReportService.listReportOptions(REPORT_URI).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).listReportOptions(REPORT_URI);
    }

    @Test
    public void should_execute_delegate_as_observable_on_list_report_options() throws Exception {
        when(mSyncDelegate.listReportOptions(anyString()))
                .thenReturn(Collections.<ReportOption>emptySet());

        TestSubscriber<Set<ReportOption>> test = TestSubscriber.create();
        rxReportService.listReportOptions(REPORT_URI).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).listReportOptions(REPORT_URI);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_create_report_option() throws Exception {
        when(mSyncDelegate.createReportOption(anyString(), anyString(), anyListOf(ReportParameter.class), anyBoolean()))
                .thenThrow(mServiceException);

        TestSubscriber<ReportOption> test = TestSubscriber.create();
        rxReportService.createReportOption(REPORT_URI, OPTION_LABEL, PARAMS, true).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).createReportOption(REPORT_URI, OPTION_LABEL, PARAMS, true);
    }

    @Test
    public void should_execute_delegate_as_observable_on_create_report_option() throws Exception {
        when(mSyncDelegate.createReportOption(anyString(), anyString(), anyListOf(ReportParameter.class), anyBoolean()))
                .thenReturn(fakeReportOption);

        TestSubscriber<ReportOption> test = TestSubscriber.create();
        rxReportService.createReportOption(REPORT_URI, OPTION_LABEL, PARAMS, true).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).createReportOption(REPORT_URI, OPTION_LABEL, PARAMS, true);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_update_report_option() throws Exception {
        doThrow(mServiceException).when(mSyncDelegate).updateReportOption(anyString(),
                anyString(), anyListOf(ReportParameter.class));

        TestSubscriber<Void> test = TestSubscriber.create();
        rxReportService.updateReportOption(REPORT_URI, OPTION_ID, PARAMS).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).updateReportOption(REPORT_URI, OPTION_ID, PARAMS);
    }

    @Test
    public void should_execute_delegate_as_observable_on_update_report_option() throws Exception {
        TestSubscriber<Void> test = TestSubscriber.create();
        rxReportService.updateReportOption(REPORT_URI, OPTION_ID, PARAMS).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).updateReportOption(REPORT_URI, OPTION_ID, PARAMS);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_delete_report_option() throws Exception {
        doThrow(mServiceException).when(mSyncDelegate).deleteReportOption(anyString(), anyString());

        TestSubscriber<Void> test = TestSubscriber.create();
        rxReportService.deleteReportOption(REPORT_URI, OPTION_ID).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).deleteReportOption(REPORT_URI, OPTION_ID);
    }

    @Test
    public void should_execute_delegate_as_observable_on_delete_report_option() throws Exception {
        TestSubscriber<Void> test = TestSubscriber.create();
        rxReportService.deleteReportOption(REPORT_URI, OPTION_ID).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).deleteReportOption(REPORT_URI, OPTION_ID);
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