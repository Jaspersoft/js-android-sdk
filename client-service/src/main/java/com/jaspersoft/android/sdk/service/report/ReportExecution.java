/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */
package com.jaspersoft.android.sdk.service.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ExportExecution;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExecution {
    private final ReportExecutionRestApi.Factory mExecutionApiFactory;
    private final ReportExportRestApi.Factory mExportApiFactory;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;

    private final String mBaseUrl;
    private final long mDelay;
    private final ReportExecutionDetailsResponse mState;

    @VisibleForTesting
    ReportExecution(
            String baseUrl,
            long delay,
            ReportExecutionRestApi.Factory executionApiFactory,
            ReportExportRestApi.Factory exportApiFactory,
            ExecutionOptionsDataMapper executionOptionsMapper,
            ReportExecutionDetailsResponse details) {
        mBaseUrl = baseUrl;
        mDelay = delay;
        mExecutionApiFactory = executionApiFactory;
        mExportApiFactory = exportApiFactory;
        mExecutionOptionsMapper = executionOptionsMapper;
        mState = details;
    }

    @NonNull
    public ReportMetadata waitForReportCompletion() {
        try {
            return performAwaitFoReport();
        } catch (ExecutionException ex) {
            throw ex.adaptToClientException();
        }
    }

    @NonNull
    public ReportExport export(RunExportCriteria criteria) {
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
        }
    }

    @NonNull
    private ReportExport performExport(RunExportCriteria criteria) {
        ReportExportExecutionResponse exportDetails = runExport(criteria);
        waitForExportReadyStatus(exportDetails);
        ReportExecutionDetailsResponse currentDetails = requestExecutionDetails();

        return createExport(currentDetails, exportDetails);
    }

    @NonNull
    private ReportExecutionDetailsResponse requestExecutionDetails() {
        return mExecutionApiFactory.get().requestReportExecutionDetails(mState.getExecutionId());
    }

    private void waitForExportReadyStatus(ReportExportExecutionResponse exportDetails) {
        final String exportId = exportDetails.getExportId();
        final String executionId = mState.getExecutionId();
        final String reportUri = mState.getReportURI();

        Status status = Status.wrap(exportDetails.getStatus());
        while (!status.isReady()) {
            if (status.isCancelled()) {
                throw ExecutionException.exportCancelled(reportUri);
            }
            if (status.isFailed()) {
                throw ExecutionException.exportFailed(reportUri);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw ExecutionException.exportFailed(reportUri, ex);
            }
            ExecutionStatusResponse exportStatus = mExportApiFactory.get()
                    .checkExportExecutionStatus(executionId, exportId);

            status = Status.wrap(exportStatus.getStatus());
        }
    }

    @NonNull
    private ReportExport createExport(ReportExecutionDetailsResponse currentDetails,
                                      ReportExportExecutionResponse exportDetails) {
        ExportExecution export = findExportExecution(currentDetails, exportDetails.getExportId());
        if (export == null) {
            throw ExecutionException.exportFailed(mState.getReportURI());
        }

        String executionId = currentDetails.getExecutionId();
        String exportId = exportDetails.getExportId();
        Collection<ReportAttachment> attachments = adaptAttachments(export);
        return new ReportExport(executionId, exportId, attachments, mExportApiFactory);
    }

    private Collection<ReportAttachment> adaptAttachments(ExportExecution export) {
        String executionId = mState.getExecutionId();
        String exportId = export.getId();
        Set<ReportOutputResource> rawAttachments = export.getAttachments();
        Collection<ReportAttachment> attachments = new ArrayList<>(rawAttachments.size());
        for (ReportOutputResource attachment : rawAttachments) {
            ReportAttachment reportAttachment = new ReportAttachment(
                    attachment.getFileName(), executionId, exportId, mExportApiFactory);
            attachments.add(reportAttachment);
        }
        return attachments;
    }

    @Nullable
    private ExportExecution findExportExecution(ReportExecutionDetailsResponse currentDetails, String exportId) {
        for (ExportExecution export : currentDetails.getExports()) {
            if (exportId.equals(export.getId())) {
                return export;
            }
        }
        return null;
    }

    @NonNull
    private ReportExportExecutionResponse runExport(RunExportCriteria criteria) {
        ExecutionRequestOptions options = mExecutionOptionsMapper.transformExportOptions(mBaseUrl, criteria);
        return mExportApiFactory.get().runExportExecution(mState.getExecutionId(), options);
    }


    @NonNull
    private ReportMetadata performAwaitFoReport() {
        ReportExecutionDetailsResponse details = requestExecutionDetails();
        ReportExecutionDetailsResponse completeDetails = waitForReportReadyStart(details);
        return new ReportMetadata(completeDetails.getReportURI(),
                completeDetails.getTotalPages());
    }

    @NonNull
    private ReportExecutionDetailsResponse waitForReportReadyStart(final ReportExecutionDetailsResponse details) {
        String reportUri = details.getReportURI();
        Status status = Status.wrap(details.getStatus());

        ReportExecutionDetailsResponse resultDetails = details;
        while (!status.isReady()) {
            if (status.isCancelled()) {
                throw ExecutionException.reportCancelled(reportUri);
            }
            if (status.isFailed()) {
                throw ExecutionException.reportFailed(reportUri);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw ExecutionException.reportFailed(reportUri, ex);
            }
            resultDetails = requestExecutionDetails();
            status = Status.wrap(details.getStatus());
        }
        return resultDetails;
    }
}
