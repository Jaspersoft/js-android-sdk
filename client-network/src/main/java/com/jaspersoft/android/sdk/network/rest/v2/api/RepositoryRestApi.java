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

import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;

import java.util.Collection;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
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

    @NonNull
    @Headers("Accept: application/repository.reportUnit+json")
    @GET("/rest_v2/resources{resourceUri}")
    ReportLookupResponse requestReportResource(@NonNull @Path(value = "resourceUri", encode = false) String resourceUri);

    class Builder extends BaseBuilder<RepositoryRestApi> {
        private final String mCookie;

        public Builder(String baseUrl, String cookie) {
            super(baseUrl);
            if (cookie == null || cookie.length() == 0) {
                throw new IllegalArgumentException("Cookie should not be null or empty");
            }
            mCookie = cookie;
        }

        public RepositoryRestApi build() {
            RestAdapter.Builder builder = getDefaultBuilder();
            builder.setConverter(new GsonConverter(GsonFactory.create()));
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("Cookie", mCookie);
                }
            });
            RestAdapter restAdapter = builder.build();

            return restAdapter.create(RepositoryRestApi.class);
        }
    }
}
