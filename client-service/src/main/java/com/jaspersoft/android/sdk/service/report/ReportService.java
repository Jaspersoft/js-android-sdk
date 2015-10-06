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
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.TokenProvider;
import com.jaspersoft.android.sdk.service.server.ServerRestApiFactory;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportService {
    private final ReportExecutionRestApi.Factory mExecutionApiFactory;
    private final ServerRestApi.Factory mInfoApiFactory;
    private final String mBaseUrl;
    private final ExecutionOptionsDataMapper mExecutionOptionsMapper;

    @VisibleForTesting
    ReportService(String baseUrl,
                  ReportExecutionRestApi.Factory executionApiFactory,
                  ServerRestApi.Factory infoApiFactory,
                  ExecutionOptionsDataMapper executionOptionsMapper) {
        mExecutionApiFactory = executionApiFactory;
        mInfoApiFactory = infoApiFactory;
        mBaseUrl = baseUrl;
        mExecutionOptionsMapper = executionOptionsMapper;
    }

    public static ReportService create(String serverUrl, TokenProvider tokenProvider) {
        ReportOptionRestApiFactory executionApiFactory = new ReportOptionRestApiFactory(serverUrl, tokenProvider);
        ServerRestApiFactory infoApiFactory = new ServerRestApiFactory(serverUrl);
        ExecutionOptionsDataMapper executionOptionsMapper = ExecutionOptionsDataMapper.getInstance();
        return new ReportService(serverUrl, executionApiFactory, infoApiFactory, executionOptionsMapper);
    }

    public ExecutionSession run(String reportUri, ExecutionConfiguration configuration) {
        ReportExecutionRequestOptions options = mExecutionOptionsMapper.transform(reportUri, mBaseUrl, configuration);
        ReportExecutionDetailsResponse details = mExecutionApiFactory.get().runReportExecution(options);
        return new ExecutionSession(mExecutionApiFactory, details);
    }
}
