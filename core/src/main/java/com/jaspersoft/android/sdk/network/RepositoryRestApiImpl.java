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

import com.jaspersoft.android.sdk.network.entity.resource.FolderLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class RepositoryRestApiImpl implements RepositoryRestApi {
    private final RestApi mRestApi;

    RepositoryRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NotNull
    @Override
    public ResourceSearchResult searchResources(@Nullable Map<String, Object> searchParams) throws IOException, HttpException {
        Iterable<?> types = null;
        Call<ResourceSearchResult> call;

        if (searchParams == null) {
            call = mRestApi.searchResources(null, null);
        } else {
            Map<String, Object> copy = new HashMap<>(searchParams);
            Object typeValues = copy.get("type");
            copy.remove("type");

            if (typeValues == null) {
                types = null;
            }
            if (typeValues instanceof Iterable<?>) {
                types = (Iterable<?>) typeValues;
            }

            call = mRestApi.searchResources(copy, types);
        }

        Response<ResourceSearchResult> rawResponse = CallWrapper.wrap(call).response();

        int status = rawResponse.code();
        ResourceSearchResult entity;
        if (status == 204) {
            entity = ResourceSearchResult.empty();
        } else {
            entity = rawResponse.body();
            com.squareup.okhttp.Headers headers = rawResponse.headers();

            int resultCount = Utils.headerToInt(headers, "Result-Count");
            int totalCount = Utils.headerToInt(headers, "Total-Count");
            int startIndex = Utils.headerToInt(headers, "Start-Index");
            int nextOffset = Utils.headerToInt(headers, "Next-Offset");

            entity.setResultCount(resultCount);
            entity.setTotalCount(totalCount);
            entity.setStartIndex(startIndex);
            entity.setNextOffset(nextOffset);
        }
        return entity;
    }

    @NotNull
    @Override
    public ReportLookup requestReportResource(@Nullable String resourceUri) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Report uri should not be null");

        Call<ReportLookup> call = mRestApi.requestReportResource(resourceUri);
        return CallWrapper.wrap(call).body();
    }

    @NotNull
    @Override
    public FolderLookup requestFolderResource(@Nullable String resourceUri) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Folder uri should not be null");

        Call<FolderLookup> call = mRestApi.requestFolderResource(resourceUri);
        return CallWrapper.wrap(call).body();
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/resources")
        Call<ResourceSearchResult> searchResources(
                @Nullable @QueryMap Map<String, Object> searchParams,
                @Nullable @Query("type") Iterable<?> types);

        @NotNull
        @Headers("Accept: application/repository.reportUnit+json")
        @GET("rest_v2/resources{resourceUri}")
        Call<ReportLookup> requestReportResource(
                @NotNull @Path(value = "resourceUri", encoded = true) String resourceUri);

        @NotNull
        @Headers("Accept: application/repository.folder+json")
        @GET("rest_v2/resources{resourceUri}")
        Call<FolderLookup> requestFolderResource(
                @NotNull @Path(value = "resourceUri", encoded = true) String resourceUri);
    }
}
