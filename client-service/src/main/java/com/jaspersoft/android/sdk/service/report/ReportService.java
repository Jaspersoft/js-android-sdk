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

import com.jaspersoft.android.sdk.network.api.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.TokenProvider;
import com.jaspersoft.android.sdk.service.server.ServerRestApiFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportService {
    private final ReportExecutionRestApi.Factory mExecutionApiFactory;
    private final ServerRestApi.Factory mInfoApiFactory;
    private final String mBaseUrl;

    public ReportService(String serverUrl, TokenProvider tokenProvider) {
        mBaseUrl = serverUrl;
        mExecutionApiFactory = new ReportOptionRestApiFactory(serverUrl, tokenProvider);
        mInfoApiFactory = new ServerRestApiFactory(serverUrl);
    }

    public ExecutionSession run(String reportUri, Map<String, Set<String>> reportParameter) {
        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest(reportUri);
        options.withBaseUrl(mBaseUrl);
        options.withParameters(reportParameter);
        options.withAsync(true);
        options.withIgnorePagination(false);

        // TODO: following parameters should be configurable
        options.withFreshData(true);
        options.withInteractive(true);
        options.withSaveDataSnapshot(true);
        options.withOutputFormat("HTML");
        options.withPages("1");

        ReportExecutionDetailsResponse details = mExecutionApiFactory.get().runReportExecution(options);
        return new ExecutionSession(mExecutionApiFactory, details);
    }
}
