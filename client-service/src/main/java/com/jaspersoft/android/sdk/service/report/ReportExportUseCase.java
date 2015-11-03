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

import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;
import com.jaspersoft.android.sdk.service.data.report.ReportOutput;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExportUseCase {
    private final ReportExportRestApi mExportApi;
    private final TokenProvider mTokenProvider;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;

    ReportExportUseCase(ReportExportRestApi exportApi, TokenProvider tokenProvider,
                        ExecutionOptionsDataMapper executionOptionsMapper) {
        mExportApi = exportApi;
        mTokenProvider = tokenProvider;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    @NotNull
    public ExportExecutionDescriptor runExport(String executionId, RunExportCriteria criteria) {
        ExecutionRequestOptions options = mExecutionOptionsMapper.transformExportOptions(criteria);
        return mExportApi.runExportExecution(mTokenProvider.provideToken(), executionId, options);
    }

    @NotNull
    public Status checkExportExecutionStatus(String executionId, String exportId) {
        ExecutionStatus exportStatus = mExportApi
                .checkExportExecutionStatus(mTokenProvider.provideToken(), executionId, exportId);
        return Status.wrap(exportStatus.getStatus());
    }

    @NotNull
    public ReportOutput requestExportOutput(String executionId, String exportId) {
        ExportOutputResource result = mExportApi.requestExportOutput(mTokenProvider.provideToken(), executionId, exportId);
        return OutputDataMapper.transform(result);
    }

    @NotNull
    public ResourceOutput requestExportAttachmentOutput(String executionId, String exportId, String fileName) {
        OutputResource result = mExportApi.requestExportAttachment(
                mTokenProvider.provideToken(),
                executionId, exportId, fileName);
        return OutputDataMapper.transform(result);
    }
}
