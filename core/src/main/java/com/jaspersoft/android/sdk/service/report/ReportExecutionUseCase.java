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
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Call;
import com.jaspersoft.android.sdk.service.internal.CallExecutor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExecutionUseCase {
    private final ReportExecutionRestApi mExecutionApi;
    private final CallExecutor mCallExecutor;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;

    ReportExecutionUseCase(ReportExecutionRestApi executionApi,
                           CallExecutor callExecutor,
                           ExecutionOptionsDataMapper executionOptionsMapper) {
        mExecutionApi = executionApi;
        mCallExecutor = callExecutor;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    @NotNull
    public ReportExecutionDescriptor runReportExecution(final String reportUri, final RunReportCriteria criteria) throws ServiceException {
        Call<ReportExecutionDescriptor> call = new Call<ReportExecutionDescriptor>() {
            @Override
            public ReportExecutionDescriptor perform(String token) throws IOException, HttpException {
                ReportExecutionRequestOptions options = mExecutionOptionsMapper.transformRunReportOptions(reportUri, criteria);
                return mExecutionApi.runReportExecution(token, options);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ExecutionStatus requestStatus(final String executionId) throws ServiceException {
        Call<ExecutionStatus> call = new Call<ExecutionStatus>() {
            @Override
            public ExecutionStatus perform(String token) throws IOException, HttpException {
                return mExecutionApi.requestReportExecutionStatus(token, executionId);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ReportExecutionDescriptor requestExecutionDetails(final String executionId) throws ServiceException {
        Call<ReportExecutionDescriptor> call = new Call<ReportExecutionDescriptor>() {
            @Override
            public ReportExecutionDescriptor perform(String token) throws IOException, HttpException {
                return mExecutionApi.requestReportExecutionDetails(token, executionId);
            }
        };
        return mCallExecutor.execute(call);
    }
}
