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
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.jaspersoft.android.sdk.service.exception.ExecutionCancelledException;
import com.jaspersoft.android.sdk.service.exception.ExecutionFailedException;

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

    public ReportExecutionDetailsResponse requestDetails() {
        return mExecutionApiFactory.get().requestReportExecutionDetails(mState.getExecutionId());
    }

    public ReportExport export(RunExportCriteria criteria) {
        final ExecutionRequestOptions options = mExecutionOptionsMapper.transformExportOptions(mBaseUrl, criteria);
        ReportExportExecutionResponse exportDetails = mExportApiFactory.get().runExportExecution(mState.getExecutionId(), options);

        final String exportId = exportDetails.getExportId();
        final String executionId = mState.getExecutionId();
        final String reportUri = mState.getReportURI();

        String status = exportDetails.getStatus();
        //  execution, ready, cancelled, failed, queued
        if (status.equals("cancelled")) {
            throw ExecutionCancelledException.forReportExport(reportUri);
        }
        if (status.equals("failed")) {
            throw ExecutionFailedException.forReportExport(reportUri);
        }
        if (status.equals("ready")) {
            return createExport(exportId);
        }

        // status is "execution" or "queued"
        while (!status.equals("ready")) {
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw ExecutionFailedException.forReportExport(reportUri, ex);
            }
            ExecutionStatusResponse exportStatus = mExportApiFactory.get()
                    .checkExportExecutionStatus(executionId, exportId);

            status = exportStatus.getStatus();
            if (status.equals("cancelled")) {
                throw ExecutionCancelledException.forReportExport(reportUri);
            }
            if (status.equals("failed")) {
                throw ExecutionFailedException.forReportExport(reportUri);
            }
        }

        return createExport(exportId);
    }

    @NonNull
    private ReportExport createExport(String exportId) {
        ReportExecutionDetailsResponse currentDetails = requestDetails();
        ExportExecution export = findExportExecution(currentDetails, exportId);
        if (export == null) {
            throw ExecutionFailedException.forReportExport(mState.getReportURI());
        }
        return new ReportExport(mState, export, mExportApiFactory);
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
}
