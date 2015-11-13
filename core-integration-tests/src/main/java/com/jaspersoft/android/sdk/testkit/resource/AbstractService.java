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

package com.jaspersoft.android.sdk.testkit.resource;

import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class AbstractService {
    private final String mToken;
    private final String mBaseUrl;
    private final OkHttpClient mClient;


    protected AbstractService(String token, String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        mClient = new OkHttpClient();
        mToken = token;
        mBaseUrl = baseUrl;
    }

    protected Response performRequest() throws IOException, HttpException {
        Request request = new Request.Builder()
                .url(provideUrl(mBaseUrl))
                .addHeader("Cookie", mToken)
                .addHeader("Accept", "application/json")
                .build();
        Response response = mClient.newCall(request).execute();

        int code = response.code();

        if (code >= 200 && code < 300) {
            return response;
        } else {
            throw HttpException.Factory.create(response);
        }
    }

    protected abstract HttpUrl provideUrl(String baseUrl);
}
