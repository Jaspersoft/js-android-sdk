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

import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.FakeCallExecutor;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ExecutionOptionsDataMapper.class})
public class ReportExecutionUseCaseTest {

    private static final String EXECUTION_ID = "execution_id";
    private static final String TOKEN = "token";

    @Mock
    ReportExecutionRestApi mExecutionRestApi;
    @Mock
    ExecutionOptionsDataMapper mDataMapper;
    @Mock
    InfoCacheManager mInfoCacheManager;
    @Mock
    ServerInfo mServerInfo;

    private ReportExecutionUseCase executionUseCase;
    private final FakeCallExecutor fakeCallExecutor = new FakeCallExecutor("token");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mInfoCacheManager.getInfo()).thenReturn(mServerInfo);
        when(mServerInfo.getVersion()).thenReturn(ServerVersion.v6);
        executionUseCase =
                new ReportExecutionUseCase(mExecutionRestApi, fakeCallExecutor, mInfoCacheManager, mDataMapper);
    }

    @Test
    public void testRunReportExecution() throws Exception {
        RunReportCriteria criteria = RunReportCriteria.builder().create();
        executionUseCase.runReportExecution("/my/uri", criteria);
        verify(mDataMapper).transformRunReportOptions("/my/uri", ServerVersion.v6, criteria);
        verify(mExecutionRestApi).runReportExecution(eq("token"), any(ReportExecutionRequestOptions.class));
    }

    @Test
    public void testRequestStatus() throws Exception {
        executionUseCase.requestStatus(EXECUTION_ID);
        verify(mExecutionRestApi).requestReportExecutionStatus(TOKEN, EXECUTION_ID);
    }

    @Test
    public void testRequestExecutionDetails() throws Exception {
        executionUseCase.requestExecutionDetails(EXECUTION_ID);
        verify(mExecutionRestApi).requestReportExecutionDetails(TOKEN, EXECUTION_ID);
    }

    @Test
    public void testUpdateExecution() throws Exception {
        List<ReportParameter> params = Collections.<ReportParameter>emptyList();
        executionUseCase.updateExecution(EXECUTION_ID, params);
        verify(mExecutionRestApi).updateReportExecution(TOKEN, EXECUTION_ID, params);
    }
}