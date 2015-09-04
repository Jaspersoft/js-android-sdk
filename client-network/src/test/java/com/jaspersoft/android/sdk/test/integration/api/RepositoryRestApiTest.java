/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.DashboardLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.FolderLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;
import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;

import org.junit.Test;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryRestApiTest {

    String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro";
    AuthResponse mAuthResponse;

    @Test
    public void shouldRequestListOfResources() throws IOException {
        RepositoryRestApi api = createApi();
        ResourceSearchResponse resourceSearchResponse = api.searchResources(null);
        assertThat(resourceSearchResponse, is(notNullValue()));
        assertThat(resourceSearchResponse.getResources(), is(not(empty())));
    }

    @Test
    public void shouldRequestReport() throws IOException {
        RepositoryRestApi api = createApi();
        Call<ReportLookupResponse> call = api.requestReportResource("/public/Samples/Reports/AllAccounts");
        Response<ReportLookupResponse> response = call.execute();
        ReportLookupResponse report = response.body();
        assertThat(report, is(notNullValue()));
        assertThat(report.getUri(), is("/public/Samples/Reports/AllAccounts"));
    }

    @Test
    public void shouldRequestDashboard() throws IOException {
        RepositoryRestApi api = createApi();
        Call<DashboardLookupResponse> call = api.requestDashboardResource("/public/Samples/Dashboards/1._Supermart_Dashboard");
        Response<DashboardLookupResponse> response = call.execute();
        DashboardLookupResponse dashboard = response.body();
        assertThat(dashboard, is(notNullValue()));
        assertThat(dashboard.getFoundations(), is(not(empty())));
    }

    @Test
    public void shouldRequestRootFolder() throws IOException {
        RepositoryRestApi api = createApi();
        Call<FolderLookupResponse> call = api.requestFolderResource("/");
        Response<FolderLookupResponse> response = call.execute();
        FolderLookupResponse folder = response.body();
        assertThat(folder, is(notNullValue()));
    }

    private RepositoryRestApi createApi() {
        return new RepositoryRestApi.Builder(mobileDemo2, getAuthResponse().getToken()).build();
    }

    private AuthResponse getAuthResponse() {
        if (mAuthResponse == null) {
            AuthenticationRestApi restApi = new AuthenticationRestApi.Builder(mobileDemo2)
//                    .setLog(TestLogger.get(this))
//                    .setLogLevel(RestApiLogLevel.FULL)
                    .build();
            mAuthResponse = restApi.authenticate("joeuser", "joeuser", "organization_1", null)
                    .toBlocking().first();
        }
        return mAuthResponse;
    }
}