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


import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.TestLogger;

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
public class ServerRestTest {

    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "servers")
    public void shouldRequestServerInfo(String baseUrl) throws Exception {
        ServerInfoData response = createApi(baseUrl).requestServerInfo();
        assertThat("Failed to receive server info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestBuild(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestBuild();
        assertThat("Failed to receive build info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestDateFormatPattern(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestDateFormatPattern();
        assertThat("Failed to receive date format info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestDateTimeFormatPattern(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestDateTimeFormatPattern();
        assertThat("Failed to receive date time format info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestEdition(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestEdition();
        assertThat("Failed to receive edition info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestEditionName(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestEditionName();
        assertThat("Failed to receive edition name info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestVersion(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestVersion();
        assertThat("Failed to receive version info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestFeatures(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestFeatures();
        assertThat("Failed to receive features info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestLicenseType(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestLicenseType();
        assertThat("Failed to receive license info response", response != null);
    }

    @Test
    @Parameters(method = "servers")
    public void shouldRequestExpiration(String baseUrl) throws Exception {
        String response = createApi(baseUrl).requestExpiration();
        assertThat("Failed to receive expiration info response", response != null);
    }

    private ServerRestApi createApi(String baseUrl) {
        return new ServerRestApi.Builder()
                .logger(TestLogger.get(this))
                .baseUrl(baseUrl)
                .build();
    }

    private Object[] servers() {
        return sEnv.listServers();
    }
}
