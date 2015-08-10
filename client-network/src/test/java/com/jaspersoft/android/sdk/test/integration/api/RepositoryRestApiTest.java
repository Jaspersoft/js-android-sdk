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

import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.DashboardLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.rest.v2.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.rest.v2.api.AuthenticationRestApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.httpclient.FakeHttp;

import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RepositoryRestApiTest {

    String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro";
    AuthResponse mAuthResponse;

    @Before
    public void setup() {
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void shouldRequestListOfResources() throws IOException {
        RepositoryRestApi api = new RepositoryRestApi.Builder(mobileDemo2, getAuthResponse().getToken()).build();
        Collection<ResourceLookupResponse> resourceLookupResponses = api.searchResources(null);
        assertThat(resourceLookupResponses, is(notNullValue()));
        assertThat(resourceLookupResponses.size(), is(not(0)));
    }

    @Test
    public void shouldRequestReport() throws IOException {
        RepositoryRestApi api = new RepositoryRestApi.Builder(mobileDemo2, getAuthResponse().getToken()).build();
        ReportLookupResponse report = api.requestReportResource("/public/Samples/Reports/AllAccounts");
        assertThat(report, is(notNullValue()));
        assertThat(report.getUri(), is("/public/Samples/Reports/AllAccounts"));
    }

    @Test
    public void shouldRequestDashboard() throws IOException {
        RepositoryRestApi api = new RepositoryRestApi.Builder(mobileDemo2, getAuthResponse().getToken()).build();
        DashboardLookupResponse dashboard = api.requestDashboardResource("/public/Samples/Dashboards/1._Supermart_Dashboard");
        assertThat(dashboard, is(notNullValue()));
        assertThat(dashboard.getFoundations().size(), is(not(0)));
    }

    private AuthResponse getAuthResponse() {
        if (mAuthResponse == null) {
            AuthenticationRestApi restApi = new AuthenticationRestApi.Builder(mobileDemo2).build();
            mAuthResponse = restApi.authenticate("joeuser", "joeuser", null, null);
        }
        return mAuthResponse;
    }
}