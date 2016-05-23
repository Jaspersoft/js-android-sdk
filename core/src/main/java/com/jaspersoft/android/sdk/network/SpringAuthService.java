/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class SpringAuthService {

    private final AuthRestApi mRestApi;
    private final JSEncryptionAlgorithm mEncryptionAlgorithm;

    @TestOnly
    SpringAuthService(
            @NotNull AuthRestApi restApi,
            @NotNull JSEncryptionAlgorithm generator) {
        mEncryptionAlgorithm = generator;
        mRestApi = restApi;
    }

    public void authenticate(SpringCredentials credentials) throws IOException, HttpException {
        String password = credentials.getPassword();
        EncryptionKey encryptionKey = mRestApi.requestEncryptionMetadata();

        if (encryptionKey.isAvailable()) {
            password = encryptPassword(credentials.getPassword(), encryptionKey);
        }

        Map<String, String> params = prepareOptionals(credentials);
        mRestApi.springAuth(
                credentials.getUsername(),
                password,
                credentials.getOrganization(),
                params);
    }

    @NotNull
    private Map<String, String> prepareOptionals(SpringCredentials credentials) {
        Map<String, String> params = new HashMap<>();
        Locale locale = credentials.getLocale();
        TimeZone timeZone = credentials.getTimeZone();

        // For Locale.US it will produce "en_US" result
        String localeAsString = locale.getLanguage() + "_" + locale.getCountry();
        // Result could be "Europe/Helsinki"
        String timeZoneAsString = timeZone.getID();

        params.put("userLocale", localeAsString);
        params.put("userTimezone", timeZoneAsString);

        return params;
    }

    @NotNull
    private String encryptPassword(String password, EncryptionKey key) {
        return mEncryptionAlgorithm.encrypt(key.getModulus(), key.getExponent(), password);
    }
}
