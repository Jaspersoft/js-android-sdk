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

package com.jaspersoft.android.sdk.util.rest.token;

import com.jaspersoft.android.sdk.util.rest.exception.AuthenticationException;
import com.jaspersoft.android.sdk.util.rest.exception.HttpException;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class TokenFactory {
    private final String mBaseUrl;
    private final OkHttpClient mOkhttp;

    public TokenFactory(String baseUrl) {
        mBaseUrl = baseUrl;
        mOkhttp = new OkHttpClient();
        mOkhttp.setFollowRedirects(false);
    }

    public String create(SpringCredentials credentials) throws IOException, HttpException {
        FormEncodingBuilder formBody = new FormEncodingBuilder()
                .add("j_password", credentials.getPassword())
                .add("j_username", credentials.getUsername());

        if (credentials.getOrganization() != null) {
            formBody.add("orgId", credentials.getOrganization());
        }
        Request request = new Request.Builder()
                .url(mBaseUrl + "j_spring_security_check")
                .post(formBody.build())
                .build();
        Response response = mOkhttp.newCall(request).execute();

        int statusCode = response.code();
        if (statusCode >= 200 && statusCode < 300) { // 2XX == successful request
            return extractCookie(response);
        } else if (statusCode >= 300 && statusCode < 400) { // 3XX == redirect request
            String location = response.headers().get("Location");
            if (location == null) {
                throw new IllegalStateException("Location HEADER is missing please contact JRS admin");
            }
            HttpUrl url = HttpUrl.parse(location);
            String errorQueryParameter = url.queryParameter("error");
            if (errorQueryParameter == null) {
                return extractCookie(response);
            } else {
                throw AuthenticationException.create(request.urlString(), response.body().string());
            }
        } else {
            throw HttpException.Factory.create(response);
        }
    }

    public static String extractCookie(Response response) {
        List<String> parts = response.headers().values("Set-Cookie");
        return joinCookieParts(parts).toString();
    }

    private static StringBuilder joinCookieParts(List<String> parts) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = parts.iterator();
        while (iterator.hasNext()) {
            String cookie = iterator.next();
            stringBuilder.append(cookie);
            if (iterator.hasNext()) {
                stringBuilder.append(";");
            }
        }
        return stringBuilder;
    }
}
