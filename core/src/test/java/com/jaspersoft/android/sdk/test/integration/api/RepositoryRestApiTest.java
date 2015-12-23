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

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.FolderLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import org.junit.BeforeClass;
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

    private final static LazyClient mLazyClient = new LazyClient(JrsMetadata.createMobileDemo2());
    private static RepositoryRestApi apiUnderTest;

    @BeforeClass
    public static void setup() {
        if (apiUnderTest == null) {
            AuthorizedClient client = mLazyClient.getAuthorizedClient();
            apiUnderTest = client.repositoryApi();
        }
    }

    @Test
    public void shouldRequestListOfResources() throws Exception {
        ResourceSearchResult resourceSearchResult = apiUnderTest.searchResources(null);
        assertThat(resourceSearchResult, is(notNullValue()));
        assertThat(resourceSearchResult.getResources(), is(not(empty())));
    }

    @Test
    public void shouldRequestReport() throws Exception {
        ReportLookup report = apiUnderTest.requestReportResource("/public/Samples/Reports/AllAccounts");
        assertThat(report, is(notNullValue()));
        assertThat(report.getUri(), is("/public/Samples/Reports/AllAccounts"));
    }

    @Test
    public void shouldRequestRootFolder() throws Exception {
        FolderLookup folder = apiUnderTest.requestFolderResource("/");
        assertThat(folder, is(notNullValue()));
    }
}