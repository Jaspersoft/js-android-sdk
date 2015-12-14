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


import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExecution {
    private final long mDelay;

    private final ReportExecutionUseCase mExecutionUseCase;
    private final ReportExportUseCase mExportUseCase;

    private final String mExecutionId;
    private final String mReportUri;
    private final ExportFactory mExportFactory;
    private final ReportService mService;
    private final RunReportCriteria mCriteria;
    private final InfoCacheManager mInfoCacheManager;

    @TestOnly
    ReportExecution(ReportService service,
                    RunReportCriteria criteria,
                    InfoCacheManager infoCacheManager,
                    long delay,
                    ReportExecutionUseCase executionUseCase,
                    ReportExportUseCase exportUseCase,
                    String executionId,
                    String reportUri) {
        mService = service;
        mCriteria = criteria;
        mInfoCacheManager = infoCacheManager;

        mDelay = delay;
        mExecutionUseCase = executionUseCase;
        mExportUseCase = exportUseCase;

        mExecutionId = executionId;
        mReportUri = reportUri;

        mExportFactory = new ExportFactory(exportUseCase, mExecutionId);
    }

    @NotNull
    public ReportMetadata waitForReportCompletion() throws ServiceException {
        return performAwaitFoReport();
    }

    @NotNull
    public ReportExecution updateExecution(List<ReportParameter> newParameters) throws ServiceException {
        ServerInfo info = mInfoCacheManager.getInfo();
        ServerVersion version = info.getVersion();
        if (version.lessThanOrEquals(ServerVersion.v5_5)) {
            RunReportCriteria criteria = mCriteria.newBuilder()
                    .params(newParameters)
                    .create();
            return mService.run(mReportUri, criteria);
        } else {
            mExecutionUseCase.updateExecution(mExecutionId, newParameters);
            mService.waitForReportExecutionStart(mReportUri, mExecutionId);
            return this;
        }
    }

    @NotNull
    public ReportExport export(RunExportCriteria criteria) throws ServiceException {
        try {
            return performExport(criteria);
        } catch (ServiceException ex) {
            boolean isCancelled = (ex.code() == StatusCodes.EXPORT_EXECUTION_CANCELLED ||
                    ex.code() == StatusCodes.REPORT_EXECUTION_CANCELLED);
            if (isCancelled) {
                /**
                 * Cancelled by technical reason. User applied Jive(for e.g. have applied new filter).
                 * Cancelled when report execution finished. This event flags that we need rerun export.
                 */
                return performExport(criteria);
            }
            throw ex;
        }
    }

    @NotNull
    private ReportMetadata performAwaitFoReport() throws ServiceException {
        ReportExecutionDescriptor details = requestExecutionDetails();
        ReportExecutionDescriptor completeDetails = waitForReportReadyStart(details);
        return new ReportMetadata(mReportUri,
                completeDetails.getTotalPages());
    }

    @NotNull
    private ReportExport performExport(RunExportCriteria criteria) throws ServiceException {
        ExportExecutionDescriptor exportDetails = runExport(criteria);
        waitForExportReadyStatus(exportDetails);
        ReportExecutionDescriptor currentDetails = requestExecutionDetails();
        return mExportFactory.create(criteria, currentDetails, exportDetails);
    }

    private void waitForExportReadyStatus(ExportExecutionDescriptor exportDetails) throws ServiceException {
        final String exportId = exportDetails.getExportId();

        Status status = Status.wrap(exportDetails.getStatus());
        ErrorDescriptor descriptor = exportDetails.getErrorDescriptor();
        while (!status.isReady()) {
            if (status.isCancelled()) {
                throw new ServiceException(
                        String.format("Export for report '%s' execution cancelled", mReportUri), null,
                        StatusCodes.EXPORT_EXECUTION_CANCELLED);
            }
            if (status.isFailed()) {
                String message = "Failed to export page";
                if (descriptor != null) {
                    message = descriptor.getMessage();
                }
                throw new ServiceException(message, null, StatusCodes.EXPORT_EXECUTION_FAILED);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw new ServiceException("Unexpected error", ex, StatusCodes.UNDEFINED_ERROR);
            }

            ExecutionStatus executionStatus = mExportUseCase.checkExportExecutionStatus(mExecutionId, exportId);
            status = Status.wrap(executionStatus.getStatus());
            descriptor = executionStatus.getErrorDescriptor();
        }
    }

    @NotNull
    private ReportExecutionDescriptor waitForReportReadyStart(final ReportExecutionDescriptor firstRunDetails) throws ServiceException {
        Status status = Status.wrap(firstRunDetails.getStatus());
        ErrorDescriptor descriptor = firstRunDetails.getErrorDescriptor();
        ReportExecutionDescriptor nextDetails = firstRunDetails;
        while (!status.isReady()) {
            if (status.isCancelled()) {
                throw new ServiceException(
                        String.format("Report '%s' execution cancelled", mReportUri), null,
                        StatusCodes.REPORT_EXECUTION_CANCELLED);
            }
            if (status.isFailed()) {
                String message = "Failed to execute report";
                if (descriptor != null) {
                    message = descriptor.getMessage();
                }
                throw new ServiceException(message, null, StatusCodes.REPORT_EXECUTION_FAILED);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw new ServiceException("Unexpected error", ex, StatusCodes.UNDEFINED_ERROR);
            }
            nextDetails = requestExecutionDetails();
            status = Status.wrap(nextDetails.getStatus());
            descriptor = nextDetails.getErrorDescriptor();
        }
        return nextDetails;
    }

    @NotNull
    private ExportExecutionDescriptor runExport(RunExportCriteria criteria) throws ServiceException {
        return mExportUseCase.runExport(mExecutionId, criteria);
    }

    @NotNull
    private ReportExecutionDescriptor requestExecutionDetails() throws ServiceException {
        return mExecutionUseCase.requestExecutionDetails(mExecutionId);
    }
}
