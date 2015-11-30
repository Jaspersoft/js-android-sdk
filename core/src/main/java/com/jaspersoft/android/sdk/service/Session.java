/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.network.ReportExecutionRestApi;
import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.service.auth.Credentials;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.service.server.InfoProvider;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Session extends AnonymousSession implements TokenProvider, InfoProvider {
    private final Credentials mCredentials;

    private ReportService mReportService;

    public Session(RestClient client, Credentials credentials) {
        super(client);
        mCredentials = credentials;
    }

    @NotNull
    public ReportService reportApi() {
        if (mReportService == null) {
            ReportExecutionRestApi executionApi = new ReportExecutionRestApi.Builder()
                    .connectionTimeOut(mClient.getConnectionTimeOut(), TimeUnit.MILLISECONDS)
                    .readTimeout(mClient.getReadTimeOut(), TimeUnit.MILLISECONDS)
                    .baseUrl(mClient.getServerUrl())
                    .build();
            ReportExportRestApi exportApi = new ReportExportRestApi.Builder()
                    .connectionTimeOut(mClient.getConnectionTimeOut(), TimeUnit.MILLISECONDS)
                    .readTimeout(mClient.getReadTimeOut(), TimeUnit.MILLISECONDS)
                    .baseUrl(mClient.getServerUrl())
                    .build();
            mReportService = ReportService.create(executionApi, exportApi, this, this);
        }
        return mReportService;
    }

    @NotNull
    @Override
    public String getBaseUrl() {
        return mClient.getServerUrl();
    }

    @Override
    public String provideToken() throws ServiceException {
        return authApi().authenticate(mCredentials);
    }

    @Override
    public double provideVersion() throws ServiceException {
        return infoApi().requestServerVersion();
    }

    @NotNull
    @Override
    public SimpleDateFormat provideDateTimeFormat() throws ServiceException {
        return infoApi().requestServerDateTimeFormat();
    }
}
