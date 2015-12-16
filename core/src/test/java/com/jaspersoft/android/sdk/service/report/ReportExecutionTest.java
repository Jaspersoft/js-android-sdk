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
import com.jaspersoft.android.sdk.network.entity.execution.ExportDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.jaspersoft.android.sdk.test.Chain.of;
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
        ExecutionOptionsDataMapper.class,
        ReportService.class,
})
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
    ReportExecutionUseCase mReportExecutionUseCase;
    @Mock
    ReportExportUseCase mReportExportUseCase;

    @Mock
    ReportService mReportService;
    @Mock
    RunReportCriteria mReportCriteria;

    @Mock
    InfoCacheManager mInfoCacheManager;
    @Mock
    ServerInfo mServerInfo;

    private ReportExecution objectUnderTest;

    @Rule
    public ExpectedException mException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mReportExecDetails1.getExecutionId()).thenReturn("execution_id");
        when(mReportExecDetails1.getReportURI()).thenReturn("/report/uri");

        when(mInfoCacheManager.getInfo()).thenReturn(mServerInfo);
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v6);

        objectUnderTest = new ReportExecution(
                mReportService,
                mReportCriteria,
                mInfoCacheManager,
                TimeUnit.SECONDS.toMillis(0),
                mReportExecutionUseCase,
                mReportExportUseCase,
                "execution_id",
                "/report/uri");
    }

    @Test(timeout = 2000)
    public void testRequestExportIdealCase() throws Exception {
        mockReportExecutionDetails("ready");
        mockRunExportExecution("ready");

        objectUnderTest.export(exportCriteria);

        verify(mReportExportUseCase).runExport(eq("execution_id"), any(RunExportCriteria.class));
        verify(mReportExecutionUseCase).requestExecutionDetails(eq("execution_id"));
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

        verify(mReportExportUseCase, times(2)).checkExportExecutionStatus(eq("execution_id"), eq("export_id"));
    }

    @Test(timeout = 2000)
    public void ensureThatExportCancelledEventWillBeResolved() throws Exception {
        mockRunExportExecution("cancelled", "ready");
        mockReportExecutionDetails("ready");

        objectUnderTest.export(exportCriteria);

        verify(mReportExportUseCase, times(2)).runExport(eq("execution_id"), any(RunExportCriteria.class));
    }

    @Test(timeout = 2000)
    public void testAwaitCompleteReport() throws Exception {
        when(mReportExecDetails1.getTotalPages()).thenReturn(100);
        mockReportExecutionDetails("ready");

        ReportMetadata metadata = objectUnderTest.waitForReportCompletion();
        assertThat(metadata.getTotalPages(), is(100));
        assertThat(metadata.getUri(), is("/report/uri"));

        verify(mReportExecutionUseCase).requestExecutionDetails(anyString());
        verifyNoMoreInteractions(mReportExecutionUseCase);
    }

    @Test(timeout = 2000)
    public void testAwaitCompleteReportShouldLoopCalls() throws Exception {
        mockReportExecutionDetails("execution", "ready");

        objectUnderTest.waitForReportCompletion();

        verify(mReportExecutionUseCase, times(2)).requestExecutionDetails(anyString());
        verifyNoMoreInteractions(mReportExecutionUseCase);
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

    @Test(timeout = 2000)
    public void testUpdateExecution() throws Exception {
        List<ReportParameter> params = Collections.<ReportParameter>emptyList();
        objectUnderTest.updateExecution(params);
        verify(mReportExecutionUseCase).updateExecution("execution_id", params);
    }

    private void mockCheckExportExecStatus(String... statusChain) throws Exception {
        ensureChain(statusChain);
        when(mExecutionStatusResponse.getStatus()).then(Chain.of(statusChain));
        when(mReportExportUseCase.checkExportExecutionStatus(anyString(), anyString())).thenReturn(mExecutionStatusResponse);
    }

    private void mockRunExportExecution(String... statusChain) throws Exception {
        ensureChain(statusChain);
        when(mExportExecDetails.getExportId()).thenReturn("export_id");
        when(mExportExecDetails.getStatus()).then(of(statusChain));
        when(mExportExecDetails.getErrorDescriptor()).thenReturn(mDescriptor);
        when(mReportExportUseCase.runExport(anyString(), any(RunExportCriteria.class))).thenReturn(mExportExecDetails);
    }

    private void mockReportExecutionDetails(String firstStatus, String... statusChain) throws Exception {
        Set<ExportDescriptor> exports = Collections.singleton(mExportExecution);
        when(mExportExecution.getStatus()).thenReturn("execution");
        when(mExportExecution.getId()).thenReturn("export_id");

        when(mReportExecDetails1.getStatus()).thenReturn(firstStatus);
        when(mReportExecDetails1.getExports()).thenReturn(exports);
        when(mReportExecDetails1.getErrorDescriptor()).thenReturn(mDescriptor);

        when(mReportExecDetails2.getStatus()).then(of(statusChain));
        when(mReportExecDetails2.getExports()).thenReturn(exports);
        when(mReportExecDetails2.getErrorDescriptor()).thenReturn(mDescriptor);

        when(mReportExecutionUseCase.requestExecutionDetails(anyString()))
                .then(of(mReportExecDetails1, mReportExecDetails2));
    }

    private void ensureChain(String[] statusChain) {
        if (statusChain.length == 0) {
            throw new IllegalArgumentException("You should supply at least one status: ready, queued, execution, cancelled, failed");
        }
    }
}