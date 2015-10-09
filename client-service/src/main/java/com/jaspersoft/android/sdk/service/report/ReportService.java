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

import android.support.annotation.VisibleForTesting;

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.TokenProvider;
import com.jaspersoft.android.sdk.service.exception.ExecutionCancelledException;
import com.jaspersoft.android.sdk.service.exception.ExecutionFailedException;
import com.jaspersoft.android.sdk.service.server.ServerRestApiFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportService {
    private final ReportExecutionRestApi.Factory mExecutionApiFactory;
    private final ReportExportRestApi.Factory mExportApiFactory;
    private final ServerRestApi.Factory mInfoApiFactory;
    private final String mBaseUrl;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;
    private final long mDelay;

    @VisibleForTesting
    ReportService(String baseUrl,
                  long delay,
                  ReportExecutionRestApi.Factory executionApiFactory,
                  ReportExportRestApi.Factory exportApiFactory,
                  ServerRestApi.Factory infoApiFactory,
                  ExecutionOptionsDataMapper executionOptionsMapper) {
        mBaseUrl = baseUrl;
        mDelay = delay;
        mExecutionApiFactory = executionApiFactory;
        mExportApiFactory = exportApiFactory;
        mInfoApiFactory = infoApiFactory;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    public static ReportService create(String serverUrl, TokenProvider tokenProvider) {
        ReportOptionRestApiFactory executionApiFactory = new ReportOptionRestApiFactory(serverUrl, tokenProvider);
        ReportExportRestApiFactory exportApiFactory = new ReportExportRestApiFactory(serverUrl, tokenProvider);
        ServerRestApiFactory infoApiFactory = new ServerRestApiFactory(serverUrl);
        ExecutionOptionsDataMapper executionOptionsMapper = ExecutionOptionsDataMapper.getInstance();
        return new ReportService(serverUrl,
                TimeUnit.SECONDS.toMillis(1),
                executionApiFactory,
                exportApiFactory,
                infoApiFactory,
                executionOptionsMapper);
    }

    public ReportExecution run(String reportUri, RunReportCriteria criteria) {
        ReportExecutionRequestOptions options = mExecutionOptionsMapper.transformRunReportOptions(reportUri, mBaseUrl, criteria);
        ReportExecutionDetailsResponse details = mExecutionApiFactory.get().runReportExecution(options);

        waitForReportExecutionStart(reportUri, details);

        return new ReportExecution(
                mBaseUrl,
                TimeUnit.SECONDS.toMillis(2),
                mExecutionApiFactory,
                mExportApiFactory,
                mExecutionOptionsMapper,
                details);
    }

    private void waitForReportExecutionStart(String reportUri, ReportExecutionDetailsResponse details) {
        String executionId = details.getExecutionId();
        Status status = Status.wrap(details.getStatus());

        while (!status.isReady() && !status.isExecution()) {
            if (status.isCancelled()) {
                throw ExecutionCancelledException.forReport(reportUri);
            }
            if (status.isFailed()) {
                throw ExecutionFailedException.forReport(reportUri);
            }
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException ex) {
                throw ExecutionFailedException.forReport(reportUri, ex);
            }
            status = Status.wrap(requestStatus(executionId).getStatus());
        }
    }

    private ExecutionStatusResponse requestStatus(String executionId) {
        return mExecutionApiFactory.get().requestReportExecutionStatus(executionId);
    }
}
