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


import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.JSException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

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

    @TestOnly
    ReportExecution(long delay,
                    ReportExecutionUseCase executionUseCase,
                    ReportExportUseCase exportUseCase,
                    String executionId,
                    String reportUri) {
        mDelay = delay;
        mExecutionUseCase = executionUseCase;
        mExportUseCase = exportUseCase;

        mExecutionId = executionId;
        mReportUri = reportUri;

        mExportFactory = new ExportFactory(exportUseCase, mExecutionId, mReportUri);
    }

    @NotNull
    public ReportMetadata waitForReportCompletion() throws JSException {
        try {
            return performAwaitFoReport();
        } catch (ExecutionException ex) {
            throw ex.adaptToClientException();
        } catch (JSException e) {
            throw e;
        }
    }

    @NotNull
    public ReportExport export(RunExportCriteria criteria) throws JSException {
        try {
            return performExport(criteria);
        } catch (ExecutionException ex) {
            if (ex.isCancelled()) {
                /**
                 * Cancelled by technical reason. User applied Jive(for e.g. have applied new filter).
                 * Cancelled when report execution finished. This event flags that we need rerun export.
                 */
                try {
                    return performExport(criteria);
                } catch (ExecutionException nestedEx) {
                    throw nestedEx.adaptToClientException();
                }
            }
            throw ex.adaptToClientException();
        } catch (JSException e) {
            throw e;
        }
    }

    @NotNull
    private ReportMetadata performAwaitFoReport() throws JSException, ExecutionException {
        ReportExecutionDescriptor details = requestExecutionDetails();
        ReportExecutionDescriptor completeDetails = waitForReportReadyStart(details);
        return new ReportMetadata(mReportUri,
                completeDetails.getTotalPages());
    }

    @NotNull
    private ReportExport performExport(RunExportCriteria criteria) throws JSException, ExecutionException {
        ExportExecutionDescriptor exportDetails = runExport(criteria);
        waitForExportReadyStatus(exportDetails);
        ReportExecutionDescriptor currentDetails = requestExecutionDetails();
        return mExportFactory.create(currentDetails, exportDetails);
    }

    private void waitForExportReadyStatus(ExportExecutionDescriptor exportDetails) throws JSException, ExecutionException{
        final String exportId = exportDetails.getExportId();

        Status status = Status.wrap(exportDetails.getStatus());
        while (!status.isReady()) {
            if (status.isCancelled()) {
                throw ExecutionException.exportCancelled(mReportUri);
            }
            if (status.isFailed()) {
                throw ExecutionException.exportFailed(mReportUri);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw ExecutionException.exportFailed(mReportUri, ex);
            }

            status = mExportUseCase.checkExportExecutionStatus(mExecutionId, exportId);
        }
    }

    @NotNull
    private ReportExecutionDescriptor waitForReportReadyStart(final ReportExecutionDescriptor details) throws JSException, ExecutionException{
        Status status = Status.wrap(details.getStatus());

        ReportExecutionDescriptor resultDetails = details;
        while (!status.isReady()) {
            if (status.isCancelled()) {
                throw ExecutionException.reportCancelled(mReportUri);
            }
            if (status.isFailed()) {
                throw ExecutionException.reportFailed(mReportUri);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw ExecutionException.reportFailed(mReportUri, ex);
            }
            resultDetails = requestExecutionDetails();
            status = Status.wrap(details.getStatus());
        }
        return resultDetails;
    }

    @NotNull
    private ExportExecutionDescriptor runExport(RunExportCriteria criteria) throws JSException {
        return mExportUseCase.runExport(mExecutionId, criteria);
    }

    @NotNull
    private ReportExecutionDescriptor requestExecutionDetails() throws JSException {
        return mExecutionUseCase.requestExecutionDetails(mExecutionId);
    }
}
