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

package com.jaspersoft.android.sdk.network.rest.v2.server;

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
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class AuthenticationRestApiImpl implements AuthenticationRestApi {
    private final RestApi mRestApi;

    AuthenticationRestApiImpl(String serverUrl) {
        RestAdapter restAdapter = createRestAdapter(serverUrl);
        mRestApi = restAdapter.create(RestApi.class);
    }

    private RestAdapter createRestAdapter(String serverUrl) {
        Endpoint endpoint = Endpoints.newFixedEndpoint(serverUrl);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setFollowRedirects(false);

        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setClient(new OkClient(httpClient));
        builder.setEndpoint(endpoint);
        builder.setErrorHandler(new retrofit.ErrorHandler() {
            @Override
            @SuppressWarnings("unchecked")
            public Throwable handleError(RetrofitError cause) {
                return ErrorHandler.DEFAULT.handleError(cause);
            }
        });

        return builder.build();
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
        @NonNull
        @Headers({"Accept:application/json"})
        @GET(value = "/j_spring_security_check")
        Response authenticate(@Query(value = "j_username", encodeValue = false) String username,
                              @Query(value = "j_password", encodeValue = false) String password,
                              @Query(value = "orgId ", encodeValue = false) String organization,
                              @QueryMap Map<String, String> params);
    }
}
