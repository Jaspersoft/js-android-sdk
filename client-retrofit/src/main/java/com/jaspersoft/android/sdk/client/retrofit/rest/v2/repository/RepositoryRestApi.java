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

package com.jaspersoft.android.sdk.client.retrofit.rest.v2.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.data.type.GsonFactory;
import com.jaspersoft.android.sdk.data.resource.ResourceLookup;

import java.util.Collection;
import java.util.Map;

import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
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
    Collection<ResourceLookup> searchResources(@Nullable @QueryMap Map<String, String> searchParams);

    class Builder {
        private final String mBaseUrl;

        public Builder(String baseUrl) {
            mBaseUrl = baseUrl;
        }

        public RepositoryRestApi build() {
            Endpoint endpoint = Endpoints.newFixedEndpoint(mBaseUrl);

            RestAdapter.Builder builder = new RestAdapter.Builder();
            builder.setEndpoint(endpoint);
            builder.setConverter(new GsonConverter(GsonFactory.create()));
            RestAdapter restAdapter = builder.build();

            return restAdapter.create(RepositoryRestApi.class);
        }
    }
}
