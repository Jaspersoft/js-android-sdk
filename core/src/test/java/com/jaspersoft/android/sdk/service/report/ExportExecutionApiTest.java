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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.test.Chain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExecutionStatus.class,
        ExportOutputResource.class,
})
public class ExportExecutionApiTest {

    private static final String EXEC_ID = "exec_id";
    private static final String EXPORT_ID = "export_id";
    private static final String ATTACHMENT_ID = "img.png";
    private static final String REPORT_URI = "my/uri";

    @Mock
    ServiceExceptionMapper mServiceExceptionMapper;
    @Mock
    ReportExportRestApi mReportExportRestApi;
    @Mock
    ExportOptionsMapper mExportOptionsMapper;

    @Mock
    AttachmentExportMapper mAttachmentExportMapper;
    @Mock
    ReportExportMapper mReportExportMapper;

    @Mock
    ExportOutputResource export;
    @Mock
    OutputResource attachment;

    @Mock
    ExecutionStatus mExecutionStatusResponse;
    @Mock
    ExecutionRequestOptions mExecutionRequestOptions;

    private ExportExecutionApiImpl exportExecutionApi;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        exportExecutionApi = new ExportExecutionApiImpl(
                mServiceExceptionMapper,
                mReportExportRestApi,
                mExportOptionsMapper,
                mReportExportMapper,
                mAttachmentExportMapper
        );
    }

    @Test
    public void testStart() throws Exception {
        when(mExportOptionsMapper.transform(any(ReportExportOptions.class))).thenReturn(mExecutionRequestOptions);

        ReportExportOptions criteria = FakeOptions.export();
        exportExecutionApi.start(EXEC_ID, criteria);
        verify(mReportExportRestApi).runExportExecution(EXEC_ID, mExecutionRequestOptions);
    }

    @Test
    public void testDownloadExport() throws Exception {
        when(mReportExportRestApi.requestExportOutput(anyString(), anyString())).thenReturn(export);

        exportExecutionApi.downloadExport(EXEC_ID, EXPORT_ID);
        verify(mReportExportRestApi).requestExportOutput(EXEC_ID, EXPORT_ID);
        verify(mReportExportMapper).transform(export);
    }

    @Test
    public void testDownloadAttachment() throws Exception {
        when(mReportExportRestApi.requestExportAttachment(anyString(), anyString(), anyString())).thenReturn(attachment);

        exportExecutionApi.downloadAttachment(EXEC_ID, EXPORT_ID, ATTACHMENT_ID);
        verify(mReportExportRestApi).requestExportAttachment(EXEC_ID, EXPORT_ID, ATTACHMENT_ID);
        verify(mAttachmentExportMapper).transform(attachment);
    }

    @Test(timeout = 2000)
    public void should_finish_if_status_ready() throws Exception {
        mockCheckExportExecStatus("ready");
        waitForReady();
        verify(mReportExportRestApi).checkExportExecutionStatus(eq(EXEC_ID), eq(EXPORT_ID));
    }

    @Test(timeout = 2000)
    public void should_throw_if_status_failed() throws Exception {
        mockCheckExportExecStatus("failed");

        try {
            waitForReady();
            fail("Should throw Status exception");
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.EXPORT_EXECUTION_FAILED));
        }
    }

    @Test(timeout = 2000)
    public void should_throw_if_status_cancelled() throws Exception {
        mockCheckExportExecStatus("cancelled");

        try {
            waitForReady();
            fail("Should throw Status exception");
        } catch (ServiceException ex) {
            assertThat(ex.code(), is(StatusCodes.EXPORT_EXECUTION_CANCELLED));
        }
    }

    @Test(timeout = 2000)
    public void should_loop_if_status_queue() throws Exception {
        mockCheckExportExecStatus("queued", "ready");
        waitForReady();
        verify(mReportExportRestApi, times(2)).checkExportExecutionStatus(eq(EXEC_ID), eq(EXPORT_ID));
    }

    @Test(timeout = 2000)
    public void should_loop_if_status_execution() throws Exception {
        mockCheckExportExecStatus("execution", "ready");
        waitForReady();
        verify(mReportExportRestApi, times(2)).checkExportExecutionStatus(eq(EXEC_ID), eq(EXPORT_ID));
    }

    private void waitForReady() throws ServiceException {
        exportExecutionApi.awaitReadyStatus(EXEC_ID, EXPORT_ID, REPORT_URI, 0);
    }

    private void mockCheckExportExecStatus(String... statusChain) throws Exception {
        when(mExecutionStatusResponse.getStatus()).then(Chain.of(statusChain));
        when(mReportExportRestApi.checkExportExecutionStatus(anyString(), anyString())).thenReturn(mExecutionStatusResponse);
    }
}