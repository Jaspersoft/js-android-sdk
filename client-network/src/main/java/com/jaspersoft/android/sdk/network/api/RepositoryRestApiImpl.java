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
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.entity.resource.DashboardLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.FolderLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.LegacyDashboardLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import static com.jaspersoft.android.sdk.network.api.Utils.checkNotNull;
import static com.jaspersoft.android.sdk.network.api.Utils.headerToInt;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class RepositoryRestApiImpl implements RepositoryRestApi {
    private final RestApi mRestApi;

    RepositoryRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public ResourceSearchResponse searchResources(@Nullable Map<String, Object> searchParams) {
        Iterable<?> types = null;
        Call<ResourceSearchResponse> call;

        if (searchParams == null) {
            call = mRestApi.searchResources(null, null);
        } else {
            Map<String, Object> copy = new HashMap<>(searchParams);
            Object typeValues = copy.get("type");
            copy.remove("type");

            if (typeValues == null) {
                throw new IllegalStateException("Found null for key 'type'. Ensure this to be not a null");
            }
            if (typeValues instanceof Iterable<?>) {
                types = (Iterable<?>) typeValues;
            }

            call = mRestApi.searchResources(copy, types);
        }

        Response<ResourceSearchResponse> rawResponse = CallWrapper.wrap(call).response();

        int status = rawResponse.code();
        ResourceSearchResponse entity;
        if (status == 204) {
            entity = ResourceSearchResponse.empty();
        } else {
            entity = rawResponse.body();
            com.squareup.okhttp.Headers headers = rawResponse.headers();

            int resultCount = headerToInt(headers, "Result-Count");
            int totalCount = headerToInt(headers, "Total-Count");
            int startIndex = headerToInt(headers, "Start-Index");
            int nextOffset = headerToInt(headers, "Next-Offset");

            entity.setResultCount(resultCount);
            entity.setTotalCount(totalCount);
            entity.setStartIndex(startIndex);
            entity.setNextOffset(nextOffset);
        }
        return entity;
    }

    @NonNull
    @Override
    public ReportLookupResponse requestReportResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Report uri should not be null");

        Call<ReportLookupResponse> call = mRestApi.requestReportResource(resourceUri);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public DashboardLookupResponse requestDashboardResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Dashboard uri should not be null");

        Call<DashboardLookupResponse> call =  mRestApi.requestDashboardResource(resourceUri);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public LegacyDashboardLookupResponse requestLegacyDashboardResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Legacy dashboard uri should not be null");

        Call<LegacyDashboardLookupResponse> call =  mRestApi.requestLegacyDashboardResource(resourceUri);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public FolderLookupResponse requestFolderResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Folder uri should not be null");

        Call<FolderLookupResponse> call =  mRestApi.requestFolderResource(resourceUri);
        return CallWrapper.wrap(call).body();
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/resources")
        Call<ResourceSearchResponse> searchResources(
                @Nullable @QueryMap Map<String, Object> searchParams,
                @Nullable @Query("type") Iterable<?> types);

        @NonNull
        @Headers("Accept: application/repository.reportUnit+json")
        @GET("rest_v2/resources{resourceUri}")
        Call<ReportLookupResponse> requestReportResource(
                @NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.dashboard+json")
        @GET("rest_v2/resources{resourceUri}")
        Call<DashboardLookupResponse> requestDashboardResource(
                @NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.legacyDashboard+json")
        @GET("rest_v2/resources{resourceUri}")
        Call<LegacyDashboardLookupResponse> requestLegacyDashboardResource(
                @NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.folder+json")
        @GET("rest_v2/resources{resourceUri}")
        Call<FolderLookupResponse> requestFolderResource(
                @NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);
    }
}
