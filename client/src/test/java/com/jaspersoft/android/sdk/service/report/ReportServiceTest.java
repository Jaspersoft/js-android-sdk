/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;
import com.jaspersoft.android.sdk.service.report.exception.ReportRunException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExecutionStatus.class,
        ReportExecutionDescriptor.class,
        ExecutionOptionsDataMapper.class})
public class ReportServiceTest {

    @Mock
    ReportExecutionRestApi executionApi;
    @Mock
    ReportExportRestApi exportApi;
    @Mock
    TokenProvider mTokenProvider;

    @Mock
    RunReportCriteria configuration;
    @Mock
    ReportExecutionDescriptor initDetails;
    @Mock
    ExecutionStatus statusDetails;

    ReportService objectUnderTest;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ExecutionOptionsDataMapper executionOptionsDataMapper = new ExecutionOptionsDataMapper("/my/uri");
        ReportExecutionUseCase reportExecutionUseCase = new ReportExecutionUseCase(executionApi, mTokenProvider, executionOptionsDataMapper);
        ReportExportUseCase exportUseCase = new ReportExportUseCase(exportApi, mTokenProvider, executionOptionsDataMapper);

        objectUnderTest = new ReportService(
                TimeUnit.MILLISECONDS.toMillis(0),
                reportExecutionUseCase,
                exportUseCase);
    }

    @Test
    public void testRunShouldCreateActiveSession() {
        mockRunReportExecution("execution");
        mockRunReportExecution("ready");

        ReportExecution session = objectUnderTest.run("/report/uri", configuration);
        assertThat(session, is(notNullValue()));

        verify(executionApi).runReportExecution(anyString(), any(ReportExecutionRequestOptions.class));
        verifyNoMoreInteractions(executionApi);
    }

    @Test
    public void testRunThrowsFailedStatusImmediately() {
        mException.expect(ReportRunException.class);
        mException.expectMessage("Report execution '/report/uri' failed on server side");

        mockRunReportExecution("failed");

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunShouldThrowFailedIfStatusFailed() {
        mException.expect(ReportRunException.class);
        mException.expectMessage("Report execution '/report/uri' failed on server side");

        mockRunReportExecution("queued");
        mockReportExecutionStatus("failed");

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunThrowsCancelledStatusImmediately() {
        mException.expect(ReportRunException.class);
        mException.expectMessage("Report execution '/report/uri' was cancelled");

        mockRunReportExecution("cancelled");

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunShouldThrowCancelledIfStatusCancelled() {
        mException.expect(ReportRunException.class);
        mException.expectMessage("Report execution '/report/uri' was cancelled");

        mockRunReportExecution("queued");
        mockReportExecutionStatus("cancelled");

        objectUnderTest.run("/report/uri", configuration);
    }

    @Test
    public void testRunShouldLoopUntilStatusExecution() {
        mockRunReportExecution("queued");
        mockReportExecutionStatus("queued", "execution");

        objectUnderTest.run("/report/uri", configuration);
        verify(executionApi, times(2)).requestReportExecutionStatus(anyString(), eq("exec_id"));
    }

    private void mockReportExecutionStatus(String... statusChain) {
        when(statusDetails.getStatus()).then(StatusChain.of(statusChain));
        when(executionApi.requestReportExecutionStatus(anyString(), anyString())).thenReturn(statusDetails);
    }

    private void mockRunReportExecution(String execution) {
        when(initDetails.getStatus()).thenReturn(execution);
        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(executionApi.runReportExecution(anyString(), any(ReportExecutionRequestOptions.class))).thenReturn(initDetails);
    }
}