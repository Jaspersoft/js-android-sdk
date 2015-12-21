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
import com.jaspersoft.android.sdk.service.data.report.ReportOutput;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Call;
import com.jaspersoft.android.sdk.service.internal.CallExecutor;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ReportExportUseCase {
    private final ReportExportRestApi mExportApi;
    private final CallExecutor mCallExecutor;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;
    private final InfoCacheManager mCacheManager;

    ReportExportUseCase(ReportExportRestApi exportApi,
                        CallExecutor callExecutor,
                        InfoCacheManager cacheManager,
                        ExecutionOptionsDataMapper executionOptionsMapper) {
        mExportApi = exportApi;
        mCallExecutor = callExecutor;
        mCacheManager = cacheManager;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    @NotNull
    public ExportExecutionDescriptor runExport(final String executionId, final RunExportCriteria criteria) throws ServiceException {
        ServerInfo info = mCacheManager.getInfo();
        final ServerVersion version = info.getVersion();

        Call<ExportExecutionDescriptor> call = new Call<ExportExecutionDescriptor>() {
            @Override
            public ExportExecutionDescriptor perform() throws IOException, HttpException {
                ExecutionRequestOptions options = mExecutionOptionsMapper.transformExportOptions(criteria, version);
                return mExportApi.runExportExecution(executionId, options);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ExecutionStatus checkExportExecutionStatus(final String executionId,
                                             final String exportId) throws ServiceException {
        ServerInfo info = mCacheManager.getInfo();
        final ServerVersion version = info.getVersion();
        if (version.lessThanOrEquals(ServerVersion.v5_5)) {
            return ExecutionStatus.readyStatus();
        }

        Call<ExecutionStatus> call = new Call<ExecutionStatus>() {
            @Override
            public ExecutionStatus perform() throws IOException, HttpException {
                return mExportApi.checkExportExecutionStatus(executionId, exportId);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ReportOutput requestExportOutput(RunExportCriteria exportCriteria,
                                            final String executionId,
                                            String exportId) throws ServiceException {
        exportId = adaptExportId(exportCriteria, exportId);
        final String resultId = exportId;

        Call<ReportOutput> call = new Call<ReportOutput>() {
            @Override
            public ReportOutput perform() throws IOException, HttpException {
                ExportOutputResource result = mExportApi.requestExportOutput(executionId, resultId);
                return OutputDataMapper.transform(result);
            }
        };
        return mCallExecutor.execute(call);
    }

    @NotNull
    public ResourceOutput requestExportAttachmentOutput(RunExportCriteria exportCriteria,
                                                        final String executionId,
                                                        String exportId,
                                                        final String fileName) throws ServiceException {
        exportId = adaptExportId(exportCriteria, exportId);
        final String resultId = exportId;

        Call<ResourceOutput> call = new Call<ResourceOutput>() {
            @Override
            public ResourceOutput perform() throws IOException, HttpException {
                OutputResource result = mExportApi.requestExportAttachment(
                        executionId, resultId, fileName);
                return OutputDataMapper.transform(result);
            }
        };
        return mCallExecutor.execute(call);
    }

    private String adaptExportId(RunExportCriteria exportCriteria, String exportId) throws ServiceException {
        ServerInfo info = mCacheManager.getInfo();
        final ServerVersion version = info.getVersion();

        if (version.lessThanOrEquals(ServerVersion.v5_5)) {
            exportId = String.format("%s;pages=%s", exportCriteria.getFormat(), exportCriteria.getPages());
            exportId = exportId.toLowerCase();
        }
        return exportId;
    }
}
