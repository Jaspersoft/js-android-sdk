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
import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.Call;
import com.jaspersoft.android.sdk.service.CallExecutor;
import com.jaspersoft.android.sdk.service.data.report.ReportOutput;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExportUseCase {
    private final ReportExportRestApi mExportApi;
    private final CallExecutor mCallExecutor;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;

    ReportExportUseCase(ReportExportRestApi exportApi,
                        CallExecutor callExecutor, ExecutionOptionsDataMapper executionOptionsMapper) {
        mExportApi = exportApi;
        mCallExecutor = callExecutor;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    @NotNull
    public ExportExecutionDescriptor runExport(final String executionId, final RunExportCriteria criteria) throws ServiceException {
        Call<ExportExecutionDescriptor> call = new Call<ExportExecutionDescriptor>() {
            @Override
            public ExportExecutionDescriptor perform(String token) throws IOException, HttpException {
                ExecutionRequestOptions options = mExecutionOptionsMapper.transformExportOptions(criteria);
                return mExportApi.runExportExecution(token, executionId, options);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public Status checkExportExecutionStatus(final String executionId, final String exportId) throws ServiceException {
        Call<Status> call = new Call<Status>() {
            @Override
            public Status perform(String token) throws IOException, HttpException {
                ExecutionStatus exportStatus = mExportApi
                        .checkExportExecutionStatus(token, executionId, exportId);
                return Status.wrap(exportStatus.getStatus());
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ReportOutput requestExportOutput(final String executionId, final String exportId) throws ServiceException {
        Call<ReportOutput> call = new Call<ReportOutput>() {
            @Override
            public ReportOutput perform(String token) throws IOException, HttpException {
                ExportOutputResource result = mExportApi.requestExportOutput(token, executionId, exportId);
                return OutputDataMapper.transform(result);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ResourceOutput requestExportAttachmentOutput(final String executionId, final String exportId,
                                                        final String fileName) throws ServiceException {
        Call<ResourceOutput> call = new Call<ResourceOutput>() {
            @Override
            public ResourceOutput perform(String token) throws IOException, HttpException {
                OutputResource result = mExportApi.requestExportAttachment(
                        token, executionId, exportId, fileName);
                return OutputDataMapper.transform(result);
            }
        };
        return mCallExecutor.execute(call);
    }
}
