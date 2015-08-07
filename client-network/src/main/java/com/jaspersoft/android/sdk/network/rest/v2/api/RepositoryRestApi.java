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

package com.jaspersoft.android.sdk.network.rest.v2.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.exception.ErrorHandler;

import java.util.Collection;
import java.util.Map;

import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.QueryMap;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface RepositoryRestApi {
    @NonNull
    @Headers("Accept: application/json")
    @GET("/rest_v2/resources")
    Collection<ResourceLookupResponse> searchResources(@Nullable @QueryMap Map<String, String> searchParams);

    class Builder {
        private final String mBaseUrl;
        private final String mCookie;

        public Builder(String baseUrl, String cookie) {
            if (baseUrl == null || baseUrl.length() == 0) {
                throw new IllegalArgumentException("Base url should not be null or empty");
            }
            if (cookie == null || cookie.length() == 0) {
                throw new IllegalArgumentException("Cookie should not be null or empty");
            }
            mBaseUrl = baseUrl;
            mCookie = cookie;
        }

        public RepositoryRestApi build() {
            Endpoint endpoint = Endpoints.newFixedEndpoint(mBaseUrl);

            RestAdapter.Builder builder = new RestAdapter.Builder();
            builder.setEndpoint(endpoint);
            builder.setConverter(new GsonConverter(GsonFactory.create()));
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Cookie", mCookie);
                }
            });
            builder.setErrorHandler(new retrofit.ErrorHandler() {
                @Override
                @SuppressWarnings("unchecked")
                public Throwable handleError(RetrofitError cause) {
                    return ErrorHandler.DEFAULT.handleError(cause);
                }
            });
            RestAdapter restAdapter = builder.build();

            return restAdapter.create(RepositoryRestApi.class);
        }
    }
}
