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


import com.jaspersoft.android.sdk.network.rest.v2.api.RestApiLogLevel;
import com.jaspersoft.android.sdk.network.rest.v2.entity.server.ServerInfoResponse;
import com.jaspersoft.android.sdk.network.rest.v2.api.ServerRestApi;
import com.jaspersoft.android.sdk.test.TestLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.httpclient.FakeHttp;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ServerRestTest {

    String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro";

    @Before
    public void setup() {
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void shouldRequestServerInfo() throws IOException {
        ServerRestApi api = new ServerRestApi.Builder(mobileDemo2)
                .setLog(TestLogger.get(this))
                .setLogLevel(RestApiLogLevel.FULL)
                .build();
        ServerInfoResponse response = api.getServerInfo();
        assertThat(response, is(notNullValue()));
    }
}
