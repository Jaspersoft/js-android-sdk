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
import com.squareup.okhttp.*;
import org.jetbrains.annotations.NotNull;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * TODO refactor following module in easy testable units
 *
 * @author Tom Koptel
 * @since 2.0
 */
final class AuthenticationRestApiImpl implements AuthenticationRestApi {
    private final HttpUrl mBaseUrl;
    private final OkHttpClient mClient;
    private final Retrofit.Builder mRestAdapterBuilder;

    AuthenticationRestApiImpl(HttpUrl baseUrl, OkHttpClient okHttpClient, Retrofit.Builder retrofit) {
        mBaseUrl = baseUrl;
        mClient = okHttpClient;
        mRestAdapterBuilder = retrofit;
    }

    @NotNull
    @Override
    public Cookies authenticate(@NotNull final String username,
                               @NotNull final String password,
                               final String organization,
                               final Map<String, String> params) throws HttpException, IOException {
        Request request = createAuthRequest(username, password, organization, params);
        Call call = mClient.newCall(request);
        com.squareup.okhttp.Response response = call.execute();
        int statusCode = response.code();
        if (statusCode >= 200 && statusCode < 300) { // 2XX == successful request
            return CookieExtractor.extract(response);
        } else if (statusCode >= 300 && statusCode < 400) { // 3XX == redirect request
            String location = response.headers().get("Location");
            HttpUrl url = HttpUrl.parse(location);
            String errorQueryParameter = url.queryParameter("error");
            if (errorQueryParameter == null) {
                return CookieExtractor.extract(response);
            } else {
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
    @Override
    public EncryptionKey requestEncryptionMetadata() throws IOException, HttpException {
        RestApi api = mRestAdapterBuilder.build().create(RestApi.class);
        Response response = CallWrapper.wrap(api.requestAnonymousCookie()).response();
        Cookies anonymousCookies = CookieExtractor.extract(response.raw());

        RestApi modifiedApi = mRestAdapterBuilder.build().create(RestApi.class);

        try {
            return CallWrapper.wrap(modifiedApi.requestEncryptionMetadata(anonymousCookies.toString())).body();
        } catch (JsonSyntaxException ex) {
            /**
             * This possible when security option is disabled on JRS side.
             * API responds with malformed json. E.g. {Error: Key generation is off}. As you can see no quotes
             * As soon as there 2 options to resolve this we decide to swallow exception and return empty object
             */
            return EncryptionKey.empty();
        }
    }

    private Request createAuthRequest(
            @NotNull final String username,
            @NotNull final String password,
            final String organization,
            final Map<String, String> params) {

        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);

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
        /**
         * Constructs url http[s]://some.jrs/j_spring_security_check
         */
        return new Request.Builder()
                .url(mBaseUrl + "j_spring_security_check")
                .post(formBody.build())
                .build();
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: text/plain")
        @GET("rest_v2/serverInfo/edition")
        retrofit.Call<String> requestAnonymousCookie();

        @NotNull
        @GET("GetEncryptionKey")
        retrofit.Call<EncryptionKey> requestEncryptionMetadata(@NotNull @Header("Cookie") String cookie);
    }
}
