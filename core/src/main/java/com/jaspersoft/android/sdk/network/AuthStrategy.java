/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class AuthStrategy {
    @NotNull
    private final Retrofit mRetrofit;
    @Nullable
    private SpringAuthService springAuthService;

    AuthStrategy(@NotNull Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    void apply(SpringCredentials credentials) throws IOException, HttpException {
        if (springAuthService == null) {
            springAuthService = createSpringAuthService();
        }
        springAuthService.authenticate(credentials);
    }

    private SpringAuthService createSpringAuthService() {
        AuthenticationRestApi restApi = new AuthenticationRestApi(mRetrofit);
        JSEncryptionAlgorithm encryptionAlgorithm = JSEncryptionAlgorithm.create();
        return new SpringAuthService(restApi, encryptionAlgorithm);
    }
}
