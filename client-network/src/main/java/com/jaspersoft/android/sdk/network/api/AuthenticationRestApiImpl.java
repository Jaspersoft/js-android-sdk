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

package com.jaspersoft.android.sdk.network.api;

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.exception.RestError;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.functions.Func0;

/**
 * TODO refactor following module in easy testable units
 *
 * @author Tom Koptel
 * @since 2.0
 */
final class AuthenticationRestApiImpl implements AuthenticationRestApi {
    private final String mBaseUrl;
    private final OkHttpClient mClient;

    AuthenticationRestApiImpl(String baseUrl, OkHttpClient okHttpClient) {
        mBaseUrl = baseUrl;
        mClient = okHttpClient;
    }

    @NonNull
    @Override
    public Observable<AuthResponse> authenticate(@NonNull final String username,
                                                 @NonNull final String password,
                                                 final String organization,
                                                 final Map<String, String> params) {
        return Observable.defer(new Func0<Observable<AuthResponse>>() {
            @Override
            public Observable<AuthResponse> call() {

                Request request = createAuthRequest(username, password, organization, params);
                Call call = mClient.newCall(request);
                try {
                    com.squareup.okhttp.Response response = call.execute();
                    int statusCode = response.code();
                    if (statusCode >= 200 && statusCode < 300) { // 2XX == successful request
                        AuthResponse authResponse = AuthResponseFactory.create(response);
                        return Observable.just(authResponse);
                    } else if (statusCode >= 300 && statusCode < 400) { // 3XX == redirect request
                        String location = response.headers().get("Location");
                        if (location == null) {
                            return Observable.error(new IllegalStateException("Location HEADER is missing please contact JRS admin"));
                        }
                        HttpUrl url = HttpUrl.parse(location);
                        String errorQueryParameter = url.queryParameter("error");
                        if (errorQueryParameter == null) {
                            AuthResponse authResponse = AuthResponseFactory.create(response);
                            return Observable.just(authResponse);
                        } else {
                            com.squareup.okhttp.Response response401 = new com.squareup.okhttp.Response.Builder()
                                    .protocol(response.protocol())
                                    .request(response.request())
                                    .headers(response.headers())
                                    .body(response.body())
                                    .code(401)
                                    .build();
                            Throwable error = RestError.httpError(response401);
                            return Observable.error(error);
                        }
                    } else if (statusCode == 401) {
                        Throwable error = RestError.httpError(response);
                        return Observable.error(error);
                    } else {
                        Throwable error = RestError.httpError(response);
                        return Observable.error(error);
                    }
                } catch (IOException e) {
                    return Observable.error(RestError.networkError(e));
                }
            }
        });
    }

    private Request createAuthRequest(
            @NonNull final String username,
            @NonNull final String password,
            final String organization,
            final Map<String, String> params) {

        OkHttpClient client = new OkHttpClient();
        client.setFollowRedirects(false);

        FormEncodingBuilder formBody = new FormEncodingBuilder()
                .add("j_username", username)
                .add("j_password", password)
                .add("orgId", organization);

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
}
