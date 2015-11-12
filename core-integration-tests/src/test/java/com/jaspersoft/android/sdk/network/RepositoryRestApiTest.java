/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

import com.jaspersoft.android.sdk.network.entity.resource.FolderLookup;
import com.jaspersoft.android.sdk.util.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.util.TestLogger;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class RepositoryRestApiTest {

    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

/*
    @Test
    public void shouldRequestListOfResources() {
        ResourceSearchResult resourceSearchResult = api.searchResources(mAuthenticator.token(), null);
        assertThat(resourceSearchResult, is(notNullValue()));
        assertThat(resourceSearchResult.getResources(), is(not(empty())));
    }

    @Test
    public void shouldRequestReport() {
        ReportLookup report = api.requestReportResource(mAuthenticator.token(), "/public/Samples/Reports/AllAccounts");
        assertThat(report, is(notNullValue()));
        assertThat(report.getUri(), is("/public/Samples/Reports/AllAccounts"));
    }
*/
    @Test
    @Parameters(method = "servers")
    public void shouldRequestRootFolder(String token, String baseUrl) {
        FolderLookup folder = createApi(baseUrl).requestFolderResource(token, "/");
        assertThat("Failed to root folder response", folder != null);
    }

    private Object[] servers() {
        return sEnv.listAuthorizedServers();
    }

    private RepositoryRestApi createApi(String baseUrl) {
        return new RepositoryRestApi.Builder()
                .baseUrl(baseUrl)
                .logger(TestLogger.get(this))
                .build();
    }
}