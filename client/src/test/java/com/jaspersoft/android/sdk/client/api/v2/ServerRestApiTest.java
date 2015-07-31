/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.client.api.v2;

import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.FakeHttp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ServerRestApiTest {

    @Before
    public void setup() {
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void shouldRequestInfoForXml() {
        ServerRestApi restApi = new ServerRestApi.Builder("http://mobiledemo.jaspersoft.com/jasperserver-pro")
                .useXmlDataType()
                .build();

        ServerInfo serverInfo = restApi.getServerInfo();
        serverInfo.setVersion(serverInfo.getVersion());

        double version = serverInfo.getVersionCode();
        assertThat(version, is(not(0d)));
    }

    @Test
    public void shouldRequestInfoForJson() {
        ServerRestApi restApi = new ServerRestApi.Builder("http://mobiledemo.jaspersoft.com/jasperserver-pro")
                .useJsonDataType()
                .build();

        ServerInfo serverInfo = restApi.getServerInfo();
        serverInfo.setVersion(serverInfo.getVersion());

        double version = serverInfo.getVersionCode();
        assertThat(version, is(not(0d)));
    }

}
