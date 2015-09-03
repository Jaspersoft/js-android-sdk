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

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.operation.PendingOperation;
import com.jaspersoft.android.sdk.network.operation.ResultCallback;
import com.squareup.okhttp.HttpUrl;

import java.io.IOException;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class AuthenticationRestApiImpl implements AuthenticationRestApi {
    private final RestApi mRestApi;

    AuthenticationRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public PendingOperation<AuthResponse> authenticate(@NonNull final String username,
                                                       @NonNull final String password,
                                                       final String organization,
                                                       final Map<String, String> params) {

        final Call call = mRestApi.authenticate(username, password, organization, params);
        PendingOperation<AuthResponse> responsePendingOperation = new PendingOperation<AuthResponse>() {
            @Override
            public AuthResponse execute() {
                try {
                    Response response = call.execute();
                    return AuthResponseFactory.create(response);
                } catch (IOException e) {
                    return AuthResponse.createFailResponse(e);
                }
            }

            @Override
            public void enqueue(final ResultCallback<AuthResponse> callback) {
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Response response) {
                        callback.onResult(AuthResponseFactory.create(response));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        callback.onResult(AuthResponse.createFailResponse(t));
                    }
                });
            }
        };

        return responsePendingOperation;
    }

//
//    private RetrofitError extractRetrofitError(RestError error) {
//        return (RetrofitError) error.getCause();
//    }

    private boolean locationPointsToSuccess(String location) {
        HttpUrl url = HttpUrl.parse(location);
        String errorQueryParameter = url.queryParameter("error");
        return errorQueryParameter == null;
    }

    @NonNull
    private String retrieveLocation(Response response) {
        String location = response.headers().get("Location");
        Utils.checkNotNull(location, "Missing 'Location' header");
        return location;
    }

    private boolean containsRedirect(Response response) {
        int status = response.code();
        return status >= 300 && status < 400;
    }

    private AuthResponse createAuthResponse(Response response) {
        return AuthResponseFactory.create(response);
    }

    interface RestApi {
        @Multipart
        @Headers({"Accept:application/json"})
        @POST(value = "/j_spring_security_check")
        Call authenticate(@Part(value = "j_username") String username,
                          @Part(value = "j_password") String password,
                          @Part(value = "orgId ") String organization,
                          @PartMap Map<String, String> params);
    }
}
