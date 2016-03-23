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

package com.jaspersoft.android.sdk.network;

import com.google.gson.JsonSyntaxException;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * TODO refactor following module in easy testable units
 *
 * @author Tom Koptel
 * @since 2.3
 */
class AuthRestApi {
    private final NetworkClient mNetworkClient;

    AuthRestApi(@NotNull NetworkClient client) {
        mNetworkClient = client;
    }

    public void springAuth(@NotNull final String username,
                           @NotNull final String password,
                           final String organization,
                           final Map<String, String> params) throws HttpException, IOException {
        FormEncodingBuilder formBody = new FormEncodingBuilder()
                .add("j_password", password)
                .add("j_username", username);
        if (organization != null) {
            formBody.add("orgId", organization);
        }
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBody.add(entry.getKey(), entry.getValue());
            }
        }

        HttpUrl url = mNetworkClient.getBaseUrl().resolve("j_spring_security_check");

        Request request = new Request.Builder()
                .url(url)
                .post(formBody.build())
                .build();

        Response response = mNetworkClient.makeCall(request);

        int statusCode = response.code();
        if (statusCode >= 300 && statusCode < 400) { // 3XX == redirect request
            String location = response.headers().get("Location");
            HttpUrl locationUrl = HttpUrl.parse(location);
            String errorQueryParameter = locationUrl.queryParameter("error");
            if (errorQueryParameter != null) {
                com.squareup.okhttp.Response response401 = new com.squareup.okhttp.Response.Builder()
                        .protocol(response.protocol())
                        .request(response.request())
                        .headers(response.headers())
                        .body(response.body())
                        .code(401)
                        .build();
                throw HttpException.httpError(response401);
            }
        } else {
            throw HttpException.httpError(response);
        }
    }

    @NotNull
    public EncryptionKey requestEncryptionMetadata() throws IOException, HttpException {
        HttpUrl url2 = mNetworkClient.getBaseUrl().resolve("GetEncryptionKey");

        Request request2 = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .url(url2)
                .get()
                .build();
        try {
            Response response = mNetworkClient.makeCall(request2);
            return mNetworkClient.deserializeJson(response, EncryptionKey.class);
        } catch (JsonSyntaxException ex) {
            /**
             * This possible when security option is disabled on JRS side.
             * API responds with malformed json. E.g. {Error: Key generation is off}. As you can see no quotes
             * As soon as there 2 options to resolve this we decide to swallow exception and return empty object
             */
            return EncryptionKey.empty();
        }
    }
}
