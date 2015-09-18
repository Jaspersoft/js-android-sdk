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

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static com.jaspersoft.android.sdk.network.api.Utils.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class CookieAuthInterceptor implements Interceptor {
    private final String mCookie;

    CookieAuthInterceptor(String cookie) {
        mCookie = cookie;
    }

    public static CookieAuthInterceptor create(String token) {
        checkNotNull(token, "Token should not be null");
        return new CookieAuthInterceptor(token);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request compressedRequest = originalRequest.newBuilder()
                .header("Cookie", mCookie)
                .build();
        return chain.proceed(compressedRequest);
    }
}
