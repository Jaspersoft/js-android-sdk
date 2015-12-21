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

import com.jaspersoft.android.sdk.network.Client;
import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.info.InfoCache;
import com.jaspersoft.android.sdk.service.internal.*;
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

    public static ReportService create(Client client, InfoCache infoCache) {
        ReportExecutionRestApi executionApi = client.reportExecutionApi();
        ReportExportRestApi exportApi = client.reportExportApi();

        ServiceExceptionMapper defaultExMapper = new DefaultExceptionMapper();
        ServiceExceptionMapper reportExMapper = new ReportExceptionMapper(defaultExMapper);
        CallExecutor callExecutor = new DefaultCallExecutor(reportExMapper);

        Server server = client.getServer();
        ExecutionOptionsDataMapper executionOptionsMapper = new ExecutionOptionsDataMapper(server.getBaseUrl());

        InfoCacheManager cacheManager = InfoCacheManager.create(server, infoCache);
        ReportExecutionUseCase reportExecutionUseCase =
                new ReportExecutionUseCase(executionApi, callExecutor, cacheManager, executionOptionsMapper);
        ReportExportUseCase reportExportUseCase =
                new ReportExportUseCase(exportApi, callExecutor, cacheManager, executionOptionsMapper);

        return new ReportService(
                TimeUnit.SECONDS.toMillis(1),
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
