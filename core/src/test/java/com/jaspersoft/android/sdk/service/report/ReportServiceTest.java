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

import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import com.jaspersoft.android.sdk.test.Chain;
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
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExecutionStatus.class,
        ErrorDescriptor.class,
        ReportExecutionDescriptor.class,
        ExecutionOptionsDataMapper.class})
public class ReportServiceTest {

    @Mock
    ErrorDescriptor mDescriptor;

    @Mock
    RunReportCriteria configuration;
    @Mock
    ReportExecutionDescriptor initDetails;
    @Mock
    ExecutionStatus statusDetails;

    @Mock
    ReportExecutionUseCase mReportExecutionUseCase;
    @Mock
    ReportExportUseCase mReportExportUseCase;

    @Mock
    InfoCacheManager mInfoCacheManager;

    ReportService objectUnderTest;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        objectUnderTest = new ReportService(
                TimeUnit.MILLISECONDS.toMillis(0),
                mInfoCacheManager,
                mReportExecutionUseCase,
                mReportExportUseCase);
    }

    @Test
    public void testRunShouldCreateActiveSession() throws Exception {
        mockRunReportExecution("queue");
        mockReportExecutionStatus("execution", "ready");

        ReportExecution session = objectUnderTest.run("/report/uri", configuration);
        assertThat(session, is(notNullValue()));

        verify(mReportExecutionUseCase).runReportExecution(anyString(), any(RunReportCriteria.class));
    }

    @Test
    public void testRunThrowsFailedStatusImmediately() throws Exception {
        mockRunReportExecution("queue");
        mockReportExecutionStatus("failed");

        try {
            objectUnderTest.run("/report/uri", configuration);
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.REPORT_EXECUTION_FAILED));
        }
    }

    @Test
    public void testRunShouldThrowFailedIfStatusFailed() throws Exception {
        mockRunReportExecution("queued");
        mockReportExecutionStatus("failed");

        try {
            objectUnderTest.run("/report/uri", configuration);
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.REPORT_EXECUTION_FAILED));
        }
    }

    @Test
    public void testRunThrowsCancelledStatusImmediately() throws Exception {
        mockRunReportExecution("queue");
        mockReportExecutionStatus("cancelled");

        try {
            objectUnderTest.run("/report/uri", configuration);
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.REPORT_EXECUTION_CANCELLED));
        }
    }

    @Test
    public void testRunShouldThrowCancelledIfStatusCancelled() throws Exception {
        mockRunReportExecution("queued");
        mockReportExecutionStatus("cancelled");

        try {
            objectUnderTest.run("/report/uri", configuration);
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.REPORT_EXECUTION_CANCELLED));
        }
    }

    @Test
    public void testRunShouldLoopUntilStatusExecution() throws Exception {
        mockRunReportExecution("queued");
        mockReportExecutionStatus("queued", "execution");

        objectUnderTest.run("/report/uri", configuration);
        verify(mReportExecutionUseCase, times(2)).requestStatus(eq("exec_id"));
    }

    private void mockReportExecutionStatus(String... statusChain) throws Exception {
        when(statusDetails.getStatus()).then(Chain.of(statusChain));
        when(statusDetails.getErrorDescriptor()).thenReturn(mDescriptor);
        when(mReportExecutionUseCase.requestStatus(anyString())).thenReturn(statusDetails);
    }

    private void mockRunReportExecution(String execution) throws Exception {
        when(initDetails.getStatus()).thenReturn(execution);
        when(initDetails.getErrorDescriptor()).thenReturn(mDescriptor);
        when(initDetails.getExecutionId()).thenReturn("exec_id");
        when(mReportExecutionUseCase.runReportExecution(anyString(), any(RunReportCriteria.class))).thenReturn(initDetails);
    }
}