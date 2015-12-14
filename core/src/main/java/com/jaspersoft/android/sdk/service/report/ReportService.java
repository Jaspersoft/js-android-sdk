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
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.service.RestClient;
import com.jaspersoft.android.sdk.service.Session;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.internal.CallExecutor;
import com.jaspersoft.android.sdk.service.internal.DefaultCallExecutor;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportService {

    private final ReportExecutionUseCase mExecutionUseCase;
    private final InfoCacheManager mInfoCacheManager;
    private final ReportExportUseCase mExportUseCase;
    private final long mDelay;

    @TestOnly
    ReportService(
            long delay,
            InfoCacheManager infoCacheManager,
            ReportExecutionUseCase executionUseCase,
            ReportExportUseCase exportUseCase) {
        mDelay = delay;
        mInfoCacheManager = infoCacheManager;
        mExecutionUseCase = executionUseCase;
        mExportUseCase = exportUseCase;
    }

    public static ReportService create(RestClient client, Session session) {
        ReportExecutionRestApi executionApi = new ReportExecutionRestApi.Builder()
                .connectionTimeOut(client.getConnectionTimeOut(), TimeUnit.MILLISECONDS)
                .readTimeout(client.getReadTimeOut(), TimeUnit.MILLISECONDS)
                .baseUrl(client.getServerUrl())
                .build();
        ReportExportRestApi exportApi = new ReportExportRestApi.Builder()
                .connectionTimeOut(client.getConnectionTimeOut(), TimeUnit.MILLISECONDS)
                .readTimeout(client.getReadTimeOut(), TimeUnit.MILLISECONDS)
                .baseUrl(client.getServerUrl())
                .build();
        CallExecutor callExecutor = DefaultCallExecutor.create(client, session);
        ExecutionOptionsDataMapper executionOptionsMapper = ExecutionOptionsDataMapper.create(client);

        InfoCacheManager cacheManager = InfoCacheManager.create(client);
        ReportExecutionUseCase reportExecutionUseCase =
                new ReportExecutionUseCase(executionApi, callExecutor, cacheManager, executionOptionsMapper);
        ReportExportUseCase reportExportUseCase =
                new ReportExportUseCase(exportApi, callExecutor, cacheManager, executionOptionsMapper);

        return new ReportService(
                client.getPollTimeout(),
                cacheManager,
                reportExecutionUseCase,
                reportExportUseCase);
    }

    public ReportExecution run(String reportUri, RunReportCriteria criteria) throws ServiceException {
        return performRun(reportUri, criteria);
    }

    @NotNull
    private ReportExecution performRun(String reportUri, RunReportCriteria criteria) throws ServiceException {
        ReportExecutionDescriptor details = mExecutionUseCase.runReportExecution(reportUri, criteria);

        waitForReportExecutionStart(reportUri, details.getExecutionId());

        return new ReportExecution(
                this,
                criteria,
                mInfoCacheManager,
                mDelay,
                mExecutionUseCase,
                mExportUseCase,
                details.getExecutionId(),
                details.getReportURI());
    }

    void waitForReportExecutionStart(String reportUri, String executionId) throws ServiceException {
        Status status;
        do {
            ExecutionStatus statusDetails = mExecutionUseCase.requestStatus(executionId);
            status = Status.wrap(statusDetails.getStatus());
            ErrorDescriptor descriptor = statusDetails.getErrorDescriptor();

            if (status.isCancelled()) {
                throw new ServiceException(
                        String.format("Report '%s' execution cancelled", reportUri), null,
                        StatusCodes.REPORT_EXECUTION_CANCELLED);
            }
            if (status.isFailed()) {
                throw new ServiceException(descriptor.getMessage(), null, StatusCodes.REPORT_EXECUTION_FAILED);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw new ServiceException("Unexpected error", ex, StatusCodes.UNDEFINED_ERROR);
            }

        } while (!status.isReady() && !status.isExecution());
    }
}
