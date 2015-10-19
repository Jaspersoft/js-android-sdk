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

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.api.JSEncryptionAlgorithm;
import com.jaspersoft.android.sdk.network.api.auth.AbstractToken;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;
import com.jaspersoft.android.sdk.test.TestLogger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.httpclient.FakeHttp;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AuthenticationRestApiTest {
    String mobileDemo2 = "http://mobiledemo2.jaspersoft.com/jasperserver-pro/";

    @Before
    public void setup() {
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Test
    public void shouldEncryptWithPassword() throws Exception {
        AuthenticationRestApi restApi = new AuthenticationRestApi.Builder()
                .baseUrl("http://192.168.88.55:8085/jasperserver-pro-61/")
                .logger(TestLogger.get(this))
                .build();
        EncryptionKey key = restApi.requestEncryptionMetadata();

        JSEncryptionAlgorithm generator = JSEncryptionAlgorithm.create(new BouncyCastleProvider());
        String cipher = generator.encrypt(key.getModulus(), key.getExponent(), "superuser");

        AbstractToken authResponse = restApi.authenticate("superuser", cipher, null, null);
        assertThat(authResponse, is(notNullValue()));
    }

    @Test
    public void shouldReturnResponseForSpringRequest() throws IOException {
        AuthenticationRestApi authApi = new AuthenticationRestApi.Builder()
                .baseUrl(mobileDemo2)
                .logger(TestLogger.get(this))
                .build();
        AbstractToken response = authApi.authenticate("joeuser", "joeuser", "organization_1", null);
        assertThat(response, is(notNullValue()));
    }
}
