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

package com.jaspersoft.android.sdk.network;

import retrofit.Retrofit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AuthorizedClient extends AnonymousClient {

    private final AnonymousClient mAnonymousClient;

    private ReportExecutionRestApi mReportExecutionRestApi;
    private ReportExportRestApi mReportExportRestApi;
    private ReportOptionRestApi mReportOptionRestApi;
    private InputControlRestApi mInputControlRestApi;
    private RepositoryRestApi mRepositoryRestApi;
    private ReportScheduleRestApi mReportScheduleRestApi;

    AuthorizedClient(Retrofit retrofit, AnonymousClient anonymousClient) {
        super(retrofit);
        mAnonymousClient = anonymousClient;
    }

    public ReportExecutionRestApi reportExecutionApi() {
        if (mReportExecutionRestApi == null) {
            mReportExecutionRestApi = new ReportExecutionRestApi(mRetrofit);
        }
        return mReportExecutionRestApi;
    }

    public ReportExportRestApi reportExportApi() {
        if (mReportExportRestApi == null) {
            mReportExportRestApi = new ReportExportRestApi(mRetrofit);
        }
        return mReportExportRestApi;
    }

    public ReportOptionRestApi reportOptionsApi() {
        if (mReportOptionRestApi == null) {
            mReportOptionRestApi = new ReportOptionRestApi(mRetrofit);
        }
        return mReportOptionRestApi;
    }

    public InputControlRestApi inputControlApi() {
        if (mInputControlRestApi == null) {
            mInputControlRestApi = new InputControlRestApi(mRetrofit);
        }
        return mInputControlRestApi;
    }

    public RepositoryRestApi repositoryApi() {
        if (mRepositoryRestApi == null) {
            mRepositoryRestApi = new RepositoryRestApi(mRetrofit);
        }
        return mRepositoryRestApi;
    }

    public ReportScheduleRestApi reportScheduleApi() {
        if (mReportScheduleRestApi == null) {
            mReportScheduleRestApi = new ReportScheduleRestApi(mRetrofit);
        }
        return mReportScheduleRestApi;
    }

    @Override
    public ServerRestApi infoApi() {
        return mAnonymousClient.infoApi();
    }

    @Override
    public AuthenticationRestApi authenticationApi() {
        return mAnonymousClient.authenticationApi();
    }
}
