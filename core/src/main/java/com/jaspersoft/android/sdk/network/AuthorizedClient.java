/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.network;

/**
 * The client that encapsulates all REST API objects that depend on JRS active session.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class AuthorizedClient extends AnonymousClient {

    private final AnonymousClient mAnonymousClient;

    private ReportExecutionRestApi mReportExecutionRestApi;
    private ReportExportRestApi mReportExportRestApi;
    private ReportOptionRestApi mReportOptionRestApi;
    private InputControlRestApi mInputControlRestApi;
    private RepositoryRestApi mRepositoryRestApi;
    private ReportScheduleRestApi mReportScheduleRestApi;
    private DashboardExportRestApi mDashboardExportRestApi;

    AuthorizedClient(NetworkClient networkClient, AnonymousClient anonymousClient) {
        super(networkClient);
        mAnonymousClient = anonymousClient;
    }

    /**
     * Provides instance of particular report execution API.
     *
     * @return api that implements report execution related calls
     */
    public ReportExecutionRestApi reportExecutionApi() {
        if (mReportExecutionRestApi == null) {
            mReportExecutionRestApi = new ReportExecutionRestApi(mNetworkClient);
        }
        return mReportExecutionRestApi;
    }

    /**
     * Provides instance of particular report export API.
     *
     * @return api that implements report export related calls
     */
    public ReportExportRestApi reportExportApi() {
        if (mReportExportRestApi == null) {
            mReportExportRestApi = new ReportExportRestApi(mNetworkClient);
        }
        return mReportExportRestApi;
    }

    /**
     * Provides instance of particular report options API.
     *
     * @return api that implements report export options calls
     */
    public ReportOptionRestApi reportOptionsApi() {
        if (mReportOptionRestApi == null) {
            mReportOptionRestApi = new ReportOptionRestApi(mNetworkClient);
        }
        return mReportOptionRestApi;
    }

    /**
     * Provides instance of particular input controls API.
     *
     * @return api that implements input controls calls
     */
    public InputControlRestApi inputControlApi() {
        if (mInputControlRestApi == null) {
            mInputControlRestApi = new InputControlRestApi(mNetworkClient);
        }
        return mInputControlRestApi;
    }

    /**
     * Provides instance of particular input controls API.
     *
     * @return api that implements input controls calls
     */
    public RepositoryRestApi repositoryApi() {
        if (mRepositoryRestApi == null) {
            mRepositoryRestApi = new RepositoryRestApi(mNetworkClient);
        }
        return mRepositoryRestApi;
    }

    /**
     * Provides instance of particular report schedule API.
     *
     * @return api that implements report schedule calls
     */
    public ReportScheduleRestApi reportScheduleApi() {
        if (mReportScheduleRestApi == null) {
            mReportScheduleRestApi = new ReportScheduleRestApi(mNetworkClient);
        }
        return mReportScheduleRestApi;
    }

    /**
     * Provides instance of particular dashboard export API.
     *
     * @return api that implements dashboard export
     */
    public DashboardExportRestApi dashboardExportApi() {
        if (mDashboardExportRestApi == null) {
            mDashboardExportRestApi = new DashboardExportRestApi(mNetworkClient);
        }
        return mDashboardExportRestApi;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServerRestApi infoApi() {
        return mAnonymousClient.infoApi();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationRestApi authenticationApi() {
        return mAnonymousClient.authenticationApi();
    }
}
