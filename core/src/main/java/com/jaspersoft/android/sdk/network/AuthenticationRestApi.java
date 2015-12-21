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
import org.jetbrains.annotations.Nullable;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.Set;

/**
 * TODO refactor following module in easy testable units
 *
 * @author Tom Koptel
 * @since 2.0
 */
class AuthenticationRestApi {
    @NotNull
    private final Client mClient;

    AuthenticationRestApi(@NotNull Client client) {
        mClient = client;
    }

    @NotNull
    public void springAuth(@NotNull final String username,
                           @NotNull final String password,
                           final String organization,
                           final Map<String, String> params) throws HttpException, IOException {
        HttpClientFactory clientFactory = mClient.getClientFactory();
        OkHttpClient okHttpClient = clientFactory.newHttpClient();
        okHttpClient.setFollowRedirects(false);

        Request request = createAuthRequest(username, password, organization, params);
        Call call = okHttpClient.newCall(request);

        com.squareup.okhttp.Response response = call.execute();

        int statusCode = response.code();
        if (statusCode >= 300 && statusCode < 400) { // 3XX == redirect request
            String location = response.headers().get("Location");
            HttpUrl url = HttpUrl.parse(location);
            String errorQueryParameter = url.queryParameter("error");
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
        RetrofitFactory retrofitFactory = mClient.getRetrofitFactory();
        HttpClientFactory clientFactory = mClient.getClientFactory();
        OkHttpClient client = clientFactory.newHttpClient();
        client.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        Retrofit retrofit = retrofitFactory.newRetrofit()
                .client(client)
                .build();

        RestApi api = retrofit.create(RestApi.class);
        CallWrapper.wrap(api.requestAnonymousCookie());

        try {
            return CallWrapper.wrap(api.requestEncryptionMetadata()).body();
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
                .url(mClient.getBaseUrl() + "j_spring_security_check")
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
        retrofit.Call<EncryptionKey> requestEncryptionMetadata();
    }
}
