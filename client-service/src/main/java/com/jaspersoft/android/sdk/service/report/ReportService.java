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
import android.support.annotation.VisibleForTesting;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.InfoProvider;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportService {
    private final ReportExecutionRestApi mExecutionApi;
    private final ReportExportRestApi mExportApi;
    private final TokenProvider mTokenProvider;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;
    private final long mDelay;

    @VisibleForTesting
    ReportService(
                  long delay,
                  ReportExecutionRestApi executionApi,
                  ReportExportRestApi exportApi,
                  TokenProvider tokenProvider,
                  ExecutionOptionsDataMapper executionOptionsMapper) {
        mDelay = delay;
        mExecutionApi = executionApi;
        mExportApi = exportApi;
        mTokenProvider = tokenProvider;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    public static ReportService create(TokenProvider tokenProvider, InfoProvider infoProvider) {
        String baseUrl = infoProvider.getBaseUrl();
        ReportExecutionRestApi executionApi = new ReportExecutionRestApi.Builder()
                .baseUrl(baseUrl)
                .build();
        ReportExportRestApi exportApi = new ReportExportRestApi.Builder()
                .baseUrl(baseUrl)
                .build();
        ExecutionOptionsDataMapper executionOptionsMapper = new ExecutionOptionsDataMapper(baseUrl);

        return new ReportService(
                TimeUnit.SECONDS.toMillis(1),
                executionApi,
                exportApi,
                tokenProvider,
                executionOptionsMapper);
    }

    public ReportExecution run(String reportUri, RunReportCriteria criteria) {
        try {
            return performRun(reportUri, criteria);
        } catch (ExecutionException ex) {
            throw ex.adaptToClientException();
        }
    }

    @NonNull
    private ReportExecution performRun(String reportUri, RunReportCriteria criteria) {
        ReportExecutionRequestOptions options = mExecutionOptionsMapper.transformRunReportOptions(reportUri, criteria);
        ReportExecutionDescriptor details = mExecutionApi.runReportExecution(mTokenProvider.provideToken(), options);

        waitForReportExecutionStart(reportUri, details);

        return new ReportExecution(
                TimeUnit.SECONDS.toMillis(2),
                mExecutionApi,
                mExportApi,
                mTokenProvider,
                mExecutionOptionsMapper,
                details);
    }

    private void waitForReportExecutionStart(String reportUri, ReportExecutionDescriptor details) {
        String executionId = details.getExecutionId();
        Status status = Status.wrap(details.getStatus());

        while (!status.isReady() && !status.isExecution()) {
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
            status = Status.wrap(requestStatus(executionId).getStatus());
        }
    }

    private ExecutionStatus requestStatus(String executionId) {
        return mExecutionApi.requestReportExecutionStatus(mTokenProvider.provideToken(), executionId);
    }
}
