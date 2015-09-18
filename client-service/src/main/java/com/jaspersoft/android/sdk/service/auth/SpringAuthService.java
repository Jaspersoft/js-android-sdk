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

package com.jaspersoft.android.sdk.service.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.api.JSEncryptionAlgorithm;
import com.jaspersoft.android.sdk.network.api.auth.CookieToken;
import com.jaspersoft.android.sdk.network.api.auth.Token;
import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

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

    @VisibleForTesting
    SpringAuthService(
            @NonNull JSEncryptionAlgorithm generator,
            @NonNull AuthenticationRestApi restApi,
            @NonNull String username,
            @NonNull String password,
            @NonNull String organization) {
        mEncryptionAlgorithm = generator;
        mRestApi = restApi;
        mUsername = username;
        mPassword = password;
        mOrganization = organization;
    }

    @NonNull
    @Override
    public Observable<Token<?>> authenticate() {
        return authenticationCall()
                .flatMap(new Func1<AuthResponse, Observable<? extends Token<?>>>() {
                    @Override
                    public Observable<? extends Token<?>> call(AuthResponse authResponse) {
                        Token<?> cookieToken = CookieToken.create(authResponse.getToken());
                        return Observable.just(cookieToken);
                    }
                });
    }

    @NonNull
    private Observable<AuthResponse> authenticationCall() {
        return Observable.defer(new Func0<Observable<AuthResponse>>() {
            @Override
            public Observable<AuthResponse> call() {
                AuthResponse response = invokeAuthentication();
                return Observable.just(response);
            }
        });
    }

    @NonNull
    private AuthResponse invokeAuthentication() {
        String password = mPassword;
        EncryptionKey encryptionKey = mRestApi.requestEncryptionMetadata();

        if (encryptionKey.isAvailable()) {
            password = encryptPassword(mPassword, encryptionKey);
        }

        return mRestApi.authenticate(mUsername, password, mOrganization, null);
    }

    @NonNull
    private String encryptPassword(String password, EncryptionKey key) {
        return mEncryptionAlgorithm.encrypt(key.getModulus(), key.getExponent(), password);
    }

    public static class Builder {
        private AuthenticationRestApi mRestApi;
        private String mUsername;
        private String mPassword;
        private String mOrganization;

        public Builder username(@NonNull String username) {
            mUsername = checkNotNull(username, "username == null");
            return this;
        }

        public Builder password(@NonNull String password) {
            mPassword = checkNotNull(password, "password == null");
            return this;
        }

        public Builder restApi(@NonNull AuthenticationRestApi restApi) {
            mRestApi = checkNotNull(restApi, "restApi == null");
            return this;
        }

        public Builder organization(@Nullable String organization) {
            mOrganization = organization;
            return this;
        }

        public Builder withDefaultApiProvider(@NonNull String serverUrl) {
            mRestApi = new AuthenticationRestApi.Builder()
                    .baseUrl(serverUrl)
                    .build();
            return this;
        }

        @NonNull
        public SpringAuthService build() {
            ensureValidState();
            JSEncryptionAlgorithm algorithm = JSEncryptionAlgorithm.create();
            return new SpringAuthService(algorithm, mRestApi, mUsername, mPassword, mOrganization);
        }

        private void ensureValidState() {
            if (mUsername == null) {
                throw new IllegalStateException("Username should not be null");
            }
            if (mPassword == null) {
                throw new IllegalStateException("Password should not be null");
            }
            if (mRestApi == null) {
                throw new IllegalStateException("Rest api should not be null. Either set it or call withDefaultApiProvider(url)");
            }
        }
    }
}
