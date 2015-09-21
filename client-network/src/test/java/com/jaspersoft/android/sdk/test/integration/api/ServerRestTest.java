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


import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;
import com.jaspersoft.android.sdk.test.TestLogger;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerRestTest {

    private String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro/";
    private ServerRestApi apiUnderTest;

    @Before
    public void setup() {
        apiUnderTest = new ServerRestApi.Builder()
                .logger(TestLogger.get(this))
                .baseUrl(mobileDemo2)
                .build();
    }

    @Test
    public void shouldRequestServerInfo() throws Exception {
        ServerInfoResponse response = apiUnderTest.requestServerInfo();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestBuild() throws Exception {
        String response = apiUnderTest.requestBuild();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestDateFormatPattern() throws Exception {
        String response = apiUnderTest.requestDateFormatPattern();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestDateTimeFormatPattern() throws Exception {
        String response = apiUnderTest.requestDateTimeFormatPattern();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestEdition() throws Exception {
        String response = apiUnderTest.requestEdition();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestEditionName() throws Exception {
        String response = apiUnderTest.requestEditionName();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestVersion() throws Exception {
        String response = apiUnderTest.requestVersion();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestFeatures() throws Exception {
        String response = apiUnderTest.requestFeatures();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestLicenseType() throws Exception {
        String response = apiUnderTest.requestLicenseType();
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRequestExpiration() throws Exception {
        String response = apiUnderTest.requestExpiration();
        assertThat(response, is(notNullValue()));
    }
}
