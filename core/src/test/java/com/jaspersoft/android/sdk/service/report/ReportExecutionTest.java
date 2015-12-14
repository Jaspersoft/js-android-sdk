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
import com.jaspersoft.android.sdk.network.entity.execution.*;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.FakeCallExecutor;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
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
        ReportExecutionDescriptor.class,
        ExecutionOptionsDataMapper.class,
        ExportExecutionDescriptor.class,
        ReportExecutionUseCase.class,
        ExportDescriptor.class,
        ExecutionStatus.class,
        ErrorDescriptor.class,
        ExecutionOptionsDataMapper.class})
public class ReportExecutionTest {

    @Mock
    RunExportCriteria exportCriteria;
    @Mock
    ExportExecutionDescriptor mExportExecDetails;
    @Mock
    ReportExecutionDescriptor mReportExecDetails1;
    @Mock
    ReportExecutionDescriptor mReportExecDetails2;
    @Mock
    ExportDescriptor mExportExecution;
    @Mock
    ExecutionStatus mExecutionStatusResponse;
    @Mock
    ErrorDescriptor mDescriptor;

    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    ReportExecutionRestApi mExecutionRestApi;

    private ReportExecution objectUnderTest;

    @Rule
    public ExpectedException mException = ExpectedException.none();
    private ReportExecutionUseCase reportExecutionUseCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mReportExecDetails1.getExecutionId()).thenReturn("execution_id");
        when(mReportExecDetails1.getReportURI()).thenReturn("/report/uri");

        ExecutionOptionsDataMapper executionOptionsDataMapper = new ExecutionOptionsDataMapper("/report/uri");
        FakeCallExecutor callExecutor = new FakeCallExecutor("cookie");
        reportExecutionUseCase = spy(
                new ReportExecutionUseCase(mExecutionRestApi, callExecutor, executionOptionsDataMapper)
        );
        ReportExportUseCase exportUseCase = new ReportExportUseCase(mExportRestApi, callExecutor, executionOptionsDataMapper);
        objectUnderTest = new ReportExecution(
                TimeUnit.SECONDS.toMillis(0),
                reportExecutionUseCase,
                exportUseCase,
                "execution_id",
                "/report/uri");
    }

    @Test(timeout = 2000)
    public void testRequestExportIdealCase() throws Exception {
        mockReportExecutionDetails("ready");
        mockRunExportExecution("ready");

        objectUnderTest.export(exportCriteria);

        verify(mExportRestApi).runExportExecution(eq("cookie"), eq("execution_id"), any(ExecutionRequestOptions.class));
        verify(mExecutionRestApi).requestReportExecutionDetails(eq("cookie"), eq("execution_id"));
    }

    @Test(timeout = 2000)
    public void testRunThrowsFailedStatusImmediately() throws Exception {
        // export run request
        mockRunExportExecution("failed");

        try {
            objectUnderTest.export(exportCriteria);
            fail("Should throw Status exception");
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.EXPORT_EXECUTION_FAILED));
        }
    }

    @Test(timeout = 2000)
    public void testRunShouldThrowFailedIfStatusFailed() throws Exception {
        mockRunExportExecution("queued");
        mockCheckExportExecStatus("failed");
        try {
            objectUnderTest.export(exportCriteria);
            fail("Should throw Status exception");
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.EXPORT_EXECUTION_FAILED));
        }
    }

    @Test(timeout = 2000)
    public void testRunThrowsCancelledStatusImmediately() throws Exception {
        // export run request
        mockRunExportExecution("cancelled");

        try {
            objectUnderTest.export(exportCriteria);
            fail("Should throw Status exception");
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.EXPORT_EXECUTION_CANCELLED));
        }
    }

    @Test(timeout = 2000)
    public void testRunShouldThrowCancelledIfStatusCancelled() throws Exception {
        mockRunExportExecution("queued");
        mockCheckExportExecStatus("cancelled");

        try {
            objectUnderTest.export(exportCriteria);
            fail("Should throw Status exception");
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.EXPORT_EXECUTION_CANCELLED));
        }
    }

    @Test(timeout = 2000)
    public void testRunReportPendingCase() throws Exception {
        mockRunExportExecution("queued");
        mockCheckExportExecStatus("queued", "ready");
        mockReportExecutionDetails("ready");

        objectUnderTest.export(exportCriteria);

        verify(mExportRestApi, times(2)).checkExportExecutionStatus(eq("cookie"), eq("execution_id"), eq("export_id"));
    }

    @Test(timeout = 2000)
    public void ensureThatExportCancelledEventWillBeResolved() throws Exception {
        mockRunExportExecution("cancelled", "ready");
        mockReportExecutionDetails("ready");

        objectUnderTest.export(exportCriteria);

        verify(mExportRestApi, times(2)).runExportExecution(eq("cookie"), eq("execution_id"), any(ExecutionRequestOptions.class));
    }

    @Test(timeout = 2000)
    public void testAwaitCompleteReport() throws Exception {
        when(mReportExecDetails1.getTotalPages()).thenReturn(100);
        mockReportExecutionDetails("ready");

        ReportMetadata metadata = objectUnderTest.waitForReportCompletion();
        assertThat(metadata.getTotalPages(), is(100));
        assertThat(metadata.getUri(), is("/report/uri"));

        verify(mExecutionRestApi).requestReportExecutionDetails(anyString(), anyString());
        verifyNoMoreInteractions(mExecutionRestApi);
    }

    @Test(timeout = 2000)
    public void testAwaitCompleteReportShouldLoopCalls() throws Exception {
        mockReportExecutionDetails("execution", "ready");

        objectUnderTest.waitForReportCompletion();

        verify(mExecutionRestApi, times(2)).requestReportExecutionDetails(anyString(), anyString());
        verifyNoMoreInteractions(mExecutionRestApi);
    }

    @Test(timeout = 2000)
    public void testAwaitCompleteReportThrowCancelledIfStatusCancelled() throws Exception {
        mockReportExecutionDetails("execution", "cancelled");

        try {
            objectUnderTest.waitForReportCompletion();
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.REPORT_EXECUTION_CANCELLED));
        }
    }

    @Test(timeout = 2000)
    public void testAwaitCompleteReportThrowFailedIfStatusFailed() throws Exception {
        mockReportExecutionDetails("execution", "failed");

        try {
            objectUnderTest.waitForReportCompletion();
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.REPORT_EXECUTION_FAILED));
        }
   }

    @Test
    public void testUpdateExecution() throws Exception {
        List<ReportParameter> params = Collections.<ReportParameter>emptyList();
        objectUnderTest.updateExecution(params);
        verify(reportExecutionUseCase).updateExecution("execution_id", params);
    }

    private void mockCheckExportExecStatus(String... statusChain) throws Exception {
        ensureChain(statusChain);
        when(mExecutionStatusResponse.getStatus()).then(Chain.of(statusChain));
        when(mExecutionStatusResponse.getErrorDescriptor()).thenReturn(mDescriptor);
        when(mExportRestApi.checkExportExecutionStatus(anyString(), anyString(), anyString())).thenReturn(mExecutionStatusResponse);
    }

    private void mockRunExportExecution(String... statusChain) throws Exception {
        ensureChain(statusChain);
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).then(Chain.of(statusChain));
        when(mExportExecDetails.getErrorDescriptor()).thenReturn(mDescriptor);
        when(mExportRestApi.runExportExecution(anyString(), anyString(), any(ExecutionRequestOptions.class))).thenReturn(mExportExecDetails);
    }

    private void mockReportExecutionDetails(String firstStatus, String... statusChain) throws Exception {
        Set<ExportDescriptor> exports = Collections.singleton(mExportExecution);
        when(mExportExecution.getStatus()).thenReturn("execution");
        when(mExportExecution.getId()).thenReturn("export_id");

        when(mReportExecDetails1.getStatus()).thenReturn(firstStatus);
        when(mReportExecDetails1.getExports()).thenReturn(exports);
        when(mReportExecDetails1.getErrorDescriptor()).thenReturn(mDescriptor);

        when(mReportExecDetails2.getStatus()).then(Chain.of(statusChain));
        when(mReportExecDetails2.getExports()).thenReturn(exports);
        when(mReportExecDetails2.getErrorDescriptor()).thenReturn(mDescriptor);

        when(mExecutionRestApi.requestReportExecutionDetails(anyString(), anyString()))
                .then(Chain.of(mReportExecDetails1, mReportExecDetails2));
    }

    private void ensureChain(String[] statusChain) {
        if (statusChain.length == 0) {
            throw new IllegalArgumentException("You should supply at least one status: ready, queued, execution, cancelled, failed");
        }
    }
}