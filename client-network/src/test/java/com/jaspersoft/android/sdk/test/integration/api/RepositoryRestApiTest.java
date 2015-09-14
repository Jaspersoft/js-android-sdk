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

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.api.auth.CookieToken;
import com.jaspersoft.android.sdk.network.entity.resource.DashboardLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.FolderLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;
import com.jaspersoft.android.sdk.test.TestLogger;
import com.jaspersoft.android.sdk.test.integration.api.utils.JrsMetadata;
import com.jaspersoft.android.sdk.test.integration.api.utils.TestAuthenticator;

import org.junit.Before;
import org.junit.Test;

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

    private final JrsMetadata mMetadata = JrsMetadata.createMobileDemo2();
    private final TestAuthenticator mAuthenticator = TestAuthenticator.newInstance(mMetadata);
    private RepositoryRestApi api;

    @Before
    public void setup() {
        mAuthenticator.authorize();
        String cookie = mAuthenticator.getCookie();

        if (api == null) {
            api = new RepositoryRestApi.Builder()
                    .token(CookieToken.newInstance(cookie))
                    .baseUrl(mMetadata.getServerUrl())
                    .logger(TestLogger.get(this))
                    .build();
        }
    }

    @Test
    public void shouldRequestListOfResources() {
        ResourceSearchResponse resourceSearchResponse = api.searchResources(null);
        assertThat(resourceSearchResponse, is(notNullValue()));
        assertThat(resourceSearchResponse.getResources(), is(not(empty())));
    }

    @Test
    public void shouldRequestReport() {
        ReportLookupResponse report = api.requestReportResource("/public/Samples/Reports/AllAccounts");
        assertThat(report, is(notNullValue()));
        assertThat(report.getUri(), is("/public/Samples/Reports/AllAccounts"));
    }

    @Test
    public void shouldRequestDashboard() {
        DashboardLookupResponse dashboard = api.requestDashboardResource("/public/Samples/Dashboards/1._Supermart_Dashboard");
        assertThat(dashboard, is(notNullValue()));
        assertThat(dashboard.getFoundations(), is(not(empty())));
    }

    @Test
    public void shouldRequestRootFolder() {
        FolderLookupResponse folder = api.requestFolderResource("/");
        assertThat(folder, is(notNullValue()));
    }

}