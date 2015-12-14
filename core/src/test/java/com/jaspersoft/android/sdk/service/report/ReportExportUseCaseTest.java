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
import com.jaspersoft.android.sdk.service.FakeCallExecutor;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus.cancelledStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ReportExportUseCaseTest {
    private static final String EXEC_ID = "exec_id";
    private static final String EXPORT_ID = "export_id";
    private static final String LEGACY_EXPORT_ID = "html;pages=1";

    @Mock
    ReportExportRestApi mExportApi;
    @Mock
    ExecutionOptionsDataMapper mExecutionOptionsMapper;

    @Mock
    InfoCacheManager mCacheManager;
    @Mock
    ServerInfo mServerInfo;

    private static final RunExportCriteria EXPORT_HTML_PAGE_1 = RunExportCriteria.builder()
            .format(ExecutionCriteria.Format.HTML)
            .pages("1")
            .create();

    private ReportExportUseCase useCase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mCacheManager.getInfo()).thenReturn(mServerInfo);
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v6);

        FakeCallExecutor callExecutor = new FakeCallExecutor("cookie");
        useCase = new ReportExportUseCase(mExportApi, callExecutor, mCacheManager, mExecutionOptionsMapper);
    }

    @Test
    public void testRequestExportOutputOnServer5_5() throws Exception {
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_5);
        useCase.requestExportOutput(EXPORT_HTML_PAGE_1, EXEC_ID, EXPORT_ID);
        verify(mExportApi).requestExportOutput(eq("cookie"), eq(EXEC_ID), eq(LEGACY_EXPORT_ID));
        verify(mCacheManager).getInfo();
        verify(mServerInfo).getVersion();
    }

    @Test
    public void testRequestExportOutputOnServer5_6() throws Exception {
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_6);
        useCase.requestExportOutput(EXPORT_HTML_PAGE_1, EXEC_ID, EXPORT_ID);
        verify(mExportApi).requestExportOutput(eq("cookie"), eq(EXEC_ID), eq(EXPORT_ID));
        verify(mCacheManager).getInfo();
    }

    @Test
    public void testRunExport() throws Exception {
        useCase.runExport(EXEC_ID, EXPORT_HTML_PAGE_1);
        verify(mCacheManager).getInfo();
        verify(mServerInfo).getVersion();
        verify(mExecutionOptionsMapper).transformExportOptions(EXPORT_HTML_PAGE_1, ServerVersion.v6);
        verify(mExportApi).runExportExecution(eq("cookie"), eq(EXEC_ID), any(ExecutionRequestOptions.class));
    }

    @Test
    public void testCheckExportExecutionStatusOnServer5_5() throws Exception {
        when(mExportApi.checkExportExecutionStatus(anyString(), anyString(), anyString())).thenReturn(cancelledStatus());
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_5);

        Status status = useCase.checkExportExecutionStatus(EXEC_ID, EXPORT_ID);
        assertThat("For server 5.5 status of export always ready", status, is(Status.wrap("ready")));
        verify(mCacheManager).getInfo();
        verify(mServerInfo).getVersion();
        verifyZeroInteractions(mExportApi);
    }

    @Test
    public void testCheckExportExecutionStatusOnServer5_6() throws Exception {
        when(mExportApi.checkExportExecutionStatus(anyString(), anyString(), anyString())).thenReturn(cancelledStatus());
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_6);

        useCase.checkExportExecutionStatus(EXEC_ID, EXPORT_ID);
        verify(mCacheManager).getInfo();
        verify(mServerInfo).getVersion();
        verify(mExportApi).checkExportExecutionStatus(eq("cookie"), eq(EXEC_ID), eq(EXPORT_ID));
    }

    @Test
    public void testRequestExportAttachmentOutputOnServer5_5() throws Exception {
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_5);

        useCase.requestExportAttachmentOutput(EXPORT_HTML_PAGE_1, EXEC_ID, EXPORT_ID, "nay");
        verify(mExportApi).requestExportAttachment(eq("cookie"), eq(EXEC_ID), eq(LEGACY_EXPORT_ID), eq("nay"));
        verify(mCacheManager).getInfo();
        verify(mServerInfo).getVersion();
    }

    @Test
    public void testRequestExportAttachmentOutputOnServer5_6() throws Exception {
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v5_6);

        useCase.requestExportAttachmentOutput(EXPORT_HTML_PAGE_1, EXEC_ID, EXPORT_ID, "nay");
        verify(mExportApi).requestExportAttachment(eq("cookie"), eq(EXEC_ID), eq(EXPORT_ID), eq("nay"));
        verify(mCacheManager).getInfo();
        verify(mServerInfo).getVersion();
    }
}