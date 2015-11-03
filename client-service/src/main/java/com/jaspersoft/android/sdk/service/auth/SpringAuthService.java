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

package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.JSEncryptionAlgorithm;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.jaspersoft.android.sdk.service.Preconditions.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class SpringAuthService implements AuthService {
    private final AuthenticationRestApi mRestApi;
    private final String mUsername;
    private final String mPassword;
    private final String mOrganization;
    private final JSEncryptionAlgorithm mEncryptionAlgorithm;
    private final Locale mLocale;
    private final TimeZone mTimeZone;

    @TestOnly
    SpringAuthService(
            @NotNull JSEncryptionAlgorithm generator,
            @NotNull AuthenticationRestApi restApi,
            @NotNull String username,
            @NotNull String password,
            @NotNull String organization,
            @NotNull Locale locale,
            @NotNull TimeZone timeZone) {
        mEncryptionAlgorithm = generator;
        mRestApi = restApi;
        mUsername = username;
        mPassword = password;
        mOrganization = organization;
        mLocale = locale;
        mTimeZone = timeZone;
    }

    @NotNull
    @Override
    public String authenticate() {
        String password = mPassword;
        EncryptionKey encryptionKey = mRestApi.requestEncryptionMetadata();

        if (encryptionKey.isAvailable()) {
            password = encryptPassword(mPassword, encryptionKey);
        }

        Map<String, String> params = prepareOptionals();
        return mRestApi.authenticate(mUsername, password, mOrganization, params);
    }

    private Map<String, String> prepareOptionals() {
        Map<String, String> params = new HashMap<>();

        // For Locale.US it will produce "en_US" result
        String locale = mLocale.getLanguage() + "_" + mLocale.getCountry();
        // Result could be "Europe/Helsinki"
        String timeZone = mTimeZone.getID();

        params.put("userLocale", locale);
        params.put("userTimezone", timeZone);

        return params;
    }

    @NotNull
    private String encryptPassword(String password, EncryptionKey key) {
        return mEncryptionAlgorithm.encrypt(key.getModulus(), key.getExponent(), password);
    }

    public static class Builder {
        private AuthenticationRestApi mRestApi;
        private String mUsername;
        private String mPassword;
        private String mOrganization;

        // Optional
        private Locale mLocale;
        private TimeZone mTimeZone;

        public Builder username(@NotNull String username) {
            mUsername = checkNotNull(username, "username == null");
            return this;
        }

        public Builder password(@NotNull String password) {
            mPassword = checkNotNull(password, "password == null");
            return this;
        }

        public Builder restApi(@NotNull AuthenticationRestApi restApi) {
            mRestApi = checkNotNull(restApi, "restApi == null");
            return this;
        }

        public Builder organization(@Nullable String organization) {
            mOrganization = organization;
            return this;
        }

        public Builder timeZone(@NotNull TimeZone timeZone) {
            mTimeZone = checkNotNull(timeZone, "timeZone == null");
            return this;
        }

        public Builder locale(@NotNull Locale locale) {
            mLocale = checkNotNull(locale, "locale == null");
            return this;
        }

        /**
         * TODO experimental API consider before release
         */
        public Builder withDefaultApiProvider(@NotNull String serverUrl) {
            mRestApi = new AuthenticationRestApi.Builder()
                    .baseUrl(serverUrl)
                    .build();
            return this;
        }

        @NotNull
        public SpringAuthService build() {
            ensureValidState();
            ensureDefaults();
            JSEncryptionAlgorithm algorithm = JSEncryptionAlgorithm.create();
            return new SpringAuthService(algorithm,
                    mRestApi,
                    mUsername,
                    mPassword,
                    mOrganization,
                    mLocale,
                    mTimeZone);
        }

        private void ensureDefaults() {
            if (mTimeZone == null) {
                mTimeZone = TimeZone.getDefault();
            }
            if (mLocale == null) {
                mLocale = Locale.getDefault();
            }
        }

        private void ensureValidState() {
            if (mUsername == null) {
                throw new IllegalStateException("Username should not be null");
            }
            if (mPassword == null) {
                throw new IllegalStateException("Password should not be null");
            }
            if (mRestApi == null) {
                throw new IllegalStateException("Rest api should not be null. Either set it or call useDefaultApi(url)");
            }
        }
    }
}
