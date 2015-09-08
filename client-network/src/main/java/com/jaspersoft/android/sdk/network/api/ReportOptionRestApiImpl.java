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
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionResponse;
import com.squareup.okhttp.Response;

import java.util.Map;
import java.util.Set;

import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;
import rx.functions.Func1;

import static com.jaspersoft.android.sdk.network.api.Utils.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportOptionRestApiImpl implements ReportOptionRestApi {
    private final RestApi mRestApi;

    private static final Func1<Response, Observable<Void>> VOID_MAP_LAMBDA = new Func1<Response, Observable<Void>>() {
        @Override
        public Observable<Void> call(Response response) {
            return Observable.empty();
        }
    };

    ReportOptionRestApiImpl(Retrofit retrofit) {
        mRestApi = retrofit.create(RestApi.class);
    }

    @NonNull
    @Override
    public Observable<ReportOptionResponse> requestReportOptionsList(@Nullable String reportUnitUri) {
        checkNotNull(reportUnitUri, "Report uri should not be null");

        return mRestApi.requestReportOptionsList(reportUnitUri)
                .onErrorResumeNext(RestErrorAdapter.<ReportOptionResponse>get());
    }

    @NonNull
    @Override
    public Observable<ReportOption> createReportOption(@Nullable String reportUnitUri,
                                                       @Nullable String optionLabel,
                                                       @Nullable Map<String, Set<String>> controlsValues,
                                                       boolean overwrite) {
        checkNotNull(reportUnitUri, "Report uri should not be null");
        checkNotNull(optionLabel, "Option label should not be null");
        checkNotNull(controlsValues, "Controls values should not be null");

        return mRestApi.createReportOption(reportUnitUri, optionLabel, controlsValues, overwrite)
                .onErrorResumeNext(RestErrorAdapter.<ReportOption>get());
    }

    @NonNull
    @Override
    public Observable<Void> updateReportOption(@Nullable String reportUnitUri,
                                               @Nullable String optionId,
                                               @Nullable Map<String, Set<String>> controlsValues) {
        checkNotNull(reportUnitUri, "Report uri should not be null");
        checkNotNull(optionId, "Option id should not be null");
        checkNotNull(controlsValues, "Controls values should not be null");

        return mRestApi.updateReportOption(reportUnitUri, optionId, controlsValues)
                .flatMap(VOID_MAP_LAMBDA)
                .onErrorResumeNext(RestErrorAdapter.<Void>get());
    }

    @NonNull
    @Override
    public Observable<Void> deleteReportOption(@Nullable String reportUnitUri,
                                               @Nullable String optionId) {
        checkNotNull(reportUnitUri, "Report uri should not be null");
        checkNotNull(optionId, "Option id should not be null");

        return mRestApi.deleteReportOption(reportUnitUri, optionId)
                .flatMap(VOID_MAP_LAMBDA)
                .onErrorResumeNext(RestErrorAdapter.<Void>get());
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitUri}/options")
        Observable<ReportOptionResponse> requestReportOptionsList(
                @NonNull @Path(value = "reportUnitUri", encoded = true) String reportUnitUri);

        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reports{reportUnitURI}/options")
        Observable<ReportOption> createReportOption(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NonNull @Query("label") String optionLabel,
                @NonNull @Body Map<String, Set<String>> controlsValues,
                @Query("overwrite") boolean overwrite);

        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reports{reportUnitURI}/options/{optionId}")
        Observable<com.squareup.okhttp.Response> updateReportOption(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NonNull @Path(value = "optionId", encoded = true) String optionId,
                @NonNull @Body Map<String, Set<String>> controlsValues);

        @NonNull
        @Headers("Accept: application/json")
        @DELETE("rest_v2/reports{reportUnitURI}/options/{optionId}")
        Observable<com.squareup.okhttp.Response> deleteReportOption(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NonNull @Path(value = "optionId", encoded = true) String optionId);
    }
}
