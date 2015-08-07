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

package com.jaspersoft.android.sdk.network.rest.v2.api;

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.rest.v2.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.rest.v2.exception.ErrorHandler;
import com.jaspersoft.android.sdk.network.rest.v2.exception.RestError;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.Map;

import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Response;
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

    AuthenticationRestApiImpl(RestAdapter restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public AuthResponse authenticate(@NonNull String username,
                                     @NonNull String password,
                                     String organization,
                                     Map<String, String> params) {
        try {
            Response response = mRestApi.authenticate(username, password, organization, params);
            return createAuthResponse(response);
        } catch (RestError error) {
            RetrofitError retrofitError = extractRetrofitError(error);
            if (retrofitError.getKind() == RetrofitError.Kind.HTTP) {
                Response response = retrofitError.getResponse();
                if (containsRedirect(response)) {
                    String location = retrieveLocation(response);

                    if (locationPointsToSuccess(location)) {
                        return createAuthResponse(response);
                    } else {
                        throw error;
                    }
                } else {
                    throw error;
                }
            } else {
                throw error;
            }
        }
    }

    private RetrofitError extractRetrofitError(RestError error) {
        return (RetrofitError) error.getCause();
    }

    private boolean locationPointsToSuccess(String location) {
        HttpUrl url = HttpUrl.parse(location);
        String errorQueryParameter = url.queryParameter("error");
        return errorQueryParameter == null;
    }

    @NonNull
    private String retrieveLocation(Response response) {
        List<Header> headers = response.getHeaders();
        for (Header header : headers) {
            if (header.getName().equals("Location")) {
                return header.getValue();
            }
        }
        throw new IllegalStateException("Missing 'Location' header");
    }

    private boolean containsRedirect(Response response) {
        int status = response.getStatus();
        return status >= 300 && status < 400;
    }

    private AuthResponse createAuthResponse(Response response) {
        return AuthResponseFactory.create(response);
    }

    interface RestApi {
        @Multipart
        @Headers({"Accept:application/json"})
        @POST(value = "/j_spring_security_check")
        Response authenticate(@Part(value = "j_username") String username,
                              @Part(value = "j_password") String password,
                              @Part(value = "orgId ") String organization,
                              @PartMap Map<String, String> params);
    }
}
