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

import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.DashboardLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.LegacyDashboardLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ResourceSearchResponse;

import java.util.Map;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class RepositoryRestApiImpl implements RepositoryRestApi {
    private final RestApi mRestApi;

    RepositoryRestApiImpl(RestAdapter restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public ResourceSearchResponse searchResources(@Nullable Map<String, String> searchParams) {
        Response response = mRestApi.searchResources(searchParams);
        int status = response.getStatus();
        if (status == 204) {
            return ResourceSearchResponseAdapter.emptyResponse();
        } else {
            return ResourceSearchResponseAdapter.adapt(response);
        }
    }

    @NonNull
    @Override
    public ReportLookupResponse requestReportResource(@NonNull String resourceUri) {
        return mRestApi.requestReportResource(resourceUri);
    }

    @NonNull
    @Override
    public DashboardLookupResponse requestDashboardResource(@NonNull String resourceUri) {
        return mRestApi.requestDashboardResource(resourceUri);
    }

    @NonNull
    @Override
    public LegacyDashboardLookupResponse requestLegacyDashboardResource(@NonNull String resourceUri) {
        return mRestApi.requestLegacyDashboardResource(resourceUri);
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/resources")
        Response searchResources(@Nullable @QueryMap Map<String, String> searchParams);

        @NonNull
        @Headers("Accept: application/repository.reportUnit+json")
        @GET("/rest_v2/resources{resourceUri}")
        ReportLookupResponse requestReportResource(@NonNull @Path(value = "resourceUri", encode = false) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.dashboard+json")
        @GET("/rest_v2/resources{resourceUri}")
        DashboardLookupResponse requestDashboardResource(@NonNull @Path(value = "resourceUri", encode = false) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.legacyDashboard+json")
        @GET("/rest_v2/resources{resourceUri}")
        LegacyDashboardLookupResponse requestLegacyDashboardResource(@NonNull @Path(value = "resourceUri", encode = false) String resourceUri);
    }
}
