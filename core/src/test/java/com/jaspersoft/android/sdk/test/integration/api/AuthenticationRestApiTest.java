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

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.JSEncryptionAlgorithm;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;
import com.jaspersoft.android.sdk.test.TestLogger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AuthenticationRestApiTest {
    String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro/";

    @Ignore
    public void shouldEncryptWithPassword() throws Exception {
        AuthenticationRestApi restApi = new AuthenticationRestApi.Builder()
                .baseUrl("http://192.168.88.55:8085/jasperserver-pro-61/")
                .logger(TestLogger.get(this))
                .build();
        EncryptionKey key = restApi.requestEncryptionMetadata();

        JSEncryptionAlgorithm generator = JSEncryptionAlgorithm.create(new BouncyCastleProvider());
        String cipher = generator.encrypt(key.getModulus(), key.getExponent(), "superuser");

        String authResponse = restApi.authenticate("superuser", cipher, null, null);
        assertThat(authResponse, is(notNullValue()));
    }

    @Test
    public void shouldReturnResponseForSpringRequest() throws Exception {
        AuthenticationRestApi authApi = new AuthenticationRestApi.Builder()
                .baseUrl(mobileDemo2)
                .logger(TestLogger.get(this))
                .build();
        String response = authApi.authenticate("joeuser", "joeuser", "organization_1", null);
        assertThat(response, is(notNullValue()));
    }
}
