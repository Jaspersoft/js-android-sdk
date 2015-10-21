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

package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.async.request.GetRootFolderDataRequest;
import com.jaspersoft.android.sdk.client.oxm.report.FolderDataResponse;
import com.jaspersoft.android.sdk.client.util.RealHttpRule;
import com.jaspersoft.android.sdk.client.util.TargetDataType;
import com.jaspersoft.android.sdk.util.StaticCacheHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.FakeHttp;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@TargetDataType(values = {"XML", "JSON"})
public class GetRootFolderDataRequestTest extends ParametrizedTest {
    @Rule
    public RealHttpRule realHttpRule = new RealHttpRule();

    @ParameterizedRobolectricTestRunner.Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return ParametrizedTest.data(GetRootFolderDataRequestTest.class);
    }

    public GetRootFolderDataRequestTest(String versionCode, String url, String dataType) {
        super(versionCode, url, dataType);
    }

    @Test
    public void shouldGetRootFolderUriMethod() throws Exception {
        StaticCacheHelper.clearCache();
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);

        GetRootFolderDataRequest request = new GetRootFolderDataRequest(getJsRestClient());
        FolderDataResponse response = request.loadDataFromNetwork();
        assertThat(response.getUri(), is("/"));
    }
}
