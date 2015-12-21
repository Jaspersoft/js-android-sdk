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

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Call;
import com.jaspersoft.android.sdk.service.internal.CallExecutor;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ReportExecutionUseCase {
    private final ReportExecutionRestApi mExecutionApi;
    private final CallExecutor mCallExecutor;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;
    private final InfoCacheManager mCacheManager;

    ReportExecutionUseCase(ReportExecutionRestApi executionApi,
                           CallExecutor callExecutor,
                           InfoCacheManager cacheManager,
                           ExecutionOptionsDataMapper executionOptionsMapper) {
        mExecutionApi = executionApi;
        mCallExecutor = callExecutor;
        mCacheManager = cacheManager;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    @NotNull
    public ReportExecutionDescriptor runReportExecution(final String reportUri, final RunReportCriteria criteria) throws ServiceException {
        ServerInfo info = mCacheManager.getInfo();
        final ServerVersion version = info.getVersion();
        Call<ReportExecutionDescriptor> call = new Call<ReportExecutionDescriptor>() {
            @Override
            public ReportExecutionDescriptor perform() throws IOException, HttpException {
                ReportExecutionRequestOptions options =
                        mExecutionOptionsMapper.transformRunReportOptions(reportUri, version, criteria);
                return mExecutionApi.runReportExecution(options);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ExecutionStatus requestStatus(final String executionId) throws ServiceException {
        Call<ExecutionStatus> call = new Call<ExecutionStatus>() {
            @Override
            public ExecutionStatus perform() throws IOException, HttpException {
                return mExecutionApi.requestReportExecutionStatus(executionId);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ReportExecutionDescriptor requestExecutionDetails(final String executionId) throws ServiceException {
        Call<ReportExecutionDescriptor> call = new Call<ReportExecutionDescriptor>() {
            @Override
            public ReportExecutionDescriptor perform() throws IOException, HttpException {
                return mExecutionApi.requestReportExecutionDetails(executionId);
            }
        };
        return mCallExecutor.execute(call);
    }

    public void updateExecution(final String executionId, final List<ReportParameter> newParameters) throws ServiceException {
        Call<Void> call = new Call<Void>() {
            @Override
            public Void perform() throws IOException, HttpException {
                mExecutionApi.updateReportExecution(executionId, newParameters);
                return null;
            }
        };
        mCallExecutor.execute(call);
    }
}
