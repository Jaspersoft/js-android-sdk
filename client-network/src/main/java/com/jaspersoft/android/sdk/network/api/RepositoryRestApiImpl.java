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

import java.util.Map;

import retrofit.HttpException;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.QueryMap;
import rx.Observable;
import rx.functions.Func1;

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
    public Observable<ResourceSearchResponse> searchResources(@Nullable Map<String, String> searchParams) {
        return mRestApi.searchResources(searchParams)
                .flatMap(new Func1<Response<ResourceSearchResponse>, Observable<Response<ResourceSearchResponse>>>() {
                    @Override
                    public Observable<Response<ResourceSearchResponse>> call(Response<ResourceSearchResponse> response) {
                        // If we user Response<?> return type we need manually handle 500 errors
                        if (response.isSuccess()) {
                            return Observable.just(response);
                        }
                        return Observable.error(new HttpException(response));
                    }
                })
                .flatMap(new Func1<Response<ResourceSearchResponse>, Observable<ResourceSearchResponse>>() {
                    @Override
                    public Observable<ResourceSearchResponse> call(Response<ResourceSearchResponse> rawResponse) {
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
                        return Observable.just(entity);
                    }
                })
                .onErrorResumeNext(RestErrorAdapter.<ResourceSearchResponse>get());
    }

    @NonNull
    @Override
    public Observable<ReportLookupResponse> requestReportResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Report uri should not be null");
        return mRestApi.requestReportResource(resourceUri)
                .onErrorResumeNext(RestErrorAdapter.<ReportLookupResponse>get());
    }

    @NonNull
    @Override
    public Observable<DashboardLookupResponse> requestDashboardResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Dashboard uri should not be null");
        return mRestApi.requestDashboardResource(resourceUri)
                .onErrorResumeNext(RestErrorAdapter.<DashboardLookupResponse>get());
    }

    @NonNull
    @Override
    public Observable<LegacyDashboardLookupResponse> requestLegacyDashboardResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Legacy dashboard uri should not be null");
        return mRestApi.requestLegacyDashboardResource(resourceUri)
                .onErrorResumeNext(RestErrorAdapter.<LegacyDashboardLookupResponse>get());
    }

    @NonNull
    @Override
    public Observable<FolderLookupResponse> requestFolderResource(@Nullable String resourceUri) {
        checkNotNull(resourceUri, "Folder uri should not be null");
        return mRestApi.requestFolderResource(resourceUri)
                .onErrorResumeNext(RestErrorAdapter.<FolderLookupResponse>get());
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/resources")
        Observable<Response<ResourceSearchResponse>> searchResources(@Nullable @QueryMap Map<String, String> searchParams);

        @NonNull
        @Headers("Accept: application/repository.reportUnit+json")
        @GET("rest_v2/resources{resourceUri}")
        Observable<ReportLookupResponse> requestReportResource(@NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.dashboard+json")
        @GET("rest_v2/resources{resourceUri}")
        Observable<DashboardLookupResponse> requestDashboardResource(@NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.legacyDashboard+json")
        @GET("rest_v2/resources{resourceUri}")
        Observable<LegacyDashboardLookupResponse> requestLegacyDashboardResource(@NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);

        @NonNull
        @Headers("Accept: application/repository.folder+json")
        @GET("rest_v2/resources{resourceUri}")
        Observable<FolderLookupResponse> requestFolderResource(@NonNull @Path(value = "resourceUri", encoded = true) String resourceUri);
    }
}
