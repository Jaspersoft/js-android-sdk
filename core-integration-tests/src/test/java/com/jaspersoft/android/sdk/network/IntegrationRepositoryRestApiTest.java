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

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ServerBundle;
import com.jaspersoft.android.sdk.env.TestLogger;
import com.jaspersoft.android.sdk.network.entity.resource.FolderLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;

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
public class IntegrationRepositoryRestApiTest {

    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "reports")
    public void shouldRequestReport(ServerBundle bundle, String reportUri) {
        ReportLookup report = createApi(bundle.getUrl()).requestReportResource(bundle.getToken(), reportUri);
        assertThat("Failed load report data", report != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestListOfResources(ServerBundle bundle) {
        ResourceSearchResult response = createApi(bundle.getUrl()).searchResources(bundle.getToken(), null);
        assertThat("Failed to perform search lookup", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestRootFolder(ServerBundle bundle) {
        FolderLookup folder = createApi(bundle.getUrl()).requestFolderResource(bundle.getToken(), "/");
        assertThat("Failed to root folder response", folder != null);
    }

    private Object[] servers() {
        return sEnv.listAuthorizedServers();
    }

    private Object[] reports() {
        return sEnv.listReports();
    }

    private RepositoryRestApi createApi(String baseUrl) {
        return new RepositoryRestApi.Builder()
                .baseUrl(baseUrl)
                .logger(TestLogger.get(this))
                .build();
    }
}