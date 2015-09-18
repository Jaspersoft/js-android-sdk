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

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.network.api.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.api.auth.CookieToken;
import com.jaspersoft.android.sdk.network.api.auth.Token;
import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class AuthService {
    private final AuthenticationRestApi mRestApi;

    public AuthService(AuthenticationRestApi restApi) {
        mRestApi = restApi;
    }

    public Observable<Token<?>> authenticate(String username, String password, String organization) {
        return makeApiCall(username, password, organization)
                .flatMap(new Func1<AuthResponse, Observable<? extends Token<?>>>() {
                    @Override
                    public Observable<? extends Token<?>> call(AuthResponse authResponse) {
                        Token<?> cookieToken = CookieToken.create(authResponse.getToken());
                        return Observable.just(cookieToken);
                    }
                });
    }

    private Observable<AuthResponse> makeApiCall(final String username, final String password, final String organization) {
        return Observable.defer(new Func0<Observable<AuthResponse>>() {
            @Override
            public Observable<AuthResponse> call() {
                AuthResponse response = mRestApi.authenticate(username, password, organization, null);
                return Observable.just(response);
            }
        });
    }
}
