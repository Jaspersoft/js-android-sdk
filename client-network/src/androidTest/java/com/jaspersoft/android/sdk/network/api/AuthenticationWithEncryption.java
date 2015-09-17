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

package com.jaspersoft.android.sdk.network.api;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionMetadata;

import java.net.URLEncoder;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AuthenticationWithEncryption extends AndroidTestCase {

    public void testEncryptionWithPassword() throws Exception {
        AuthenticationRestApi restApi = new AuthenticationRestApi.Builder()
                .baseUrl("http://192.168.88.55:8085/jasperserver-pro-61/")
                .logger(new RestApiLog() {
                    @Override
                    public void log(String message) {
                        Log.d(AuthenticationWithEncryption.class.getSimpleName(), message);
                    }
                })
                .build();
        EncryptionMetadata metadata = restApi.requestEncryptionMetadata();

        PasswordEncryption generator = new PasswordEncryption(
                metadata.getModulus(),
                metadata.getExponent());
        String cipher = generator.encrypt(URLEncoder.encode("superuser", "UTF-8"));

        AuthResponse authResponse = restApi.authenticate("superuser", cipher, null, null);
        assertTrue(authResponse != null);
    }
}
