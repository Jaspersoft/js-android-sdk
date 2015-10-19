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

import com.google.gson.JsonSyntaxException;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionSet;
import com.squareup.okhttp.Response;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

import static com.jaspersoft.android.sdk.network.api.Utils.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportOptionRestApiImpl implements ReportOptionRestApi {
    private final RestApi mRestApi;

    ReportOptionRestApiImpl(Retrofit retrofit) {
        mRestApi = retrofit.create(RestApi.class);
    }

    @NonNull
    @Override
    public Set<ReportOption> requestReportOptionsList(@Nullable String reportUnitUri) {
        checkNotNull(reportUnitUri, "Report uri should not be null");

        Call<ReportOptionSet> call = mRestApi.requestReportOptionsList(reportUnitUri);
        try {
            ReportOptionSet options = CallWrapper.wrap(call).body();
            return options.get();
        } catch (JsonSyntaxException ex) {
            /**
             * This possible when there is no report options
             * API responds with plain/text message: 'No options found for {URI}'
             * As soon as there 2 options to reserve this we decide to swallow exception and return empty object
             */
            return Collections.emptySet();
        }
    }

    @NonNull
    @Override
    public ReportOption createReportOption(@Nullable String reportUnitUri,
                                                       @Nullable String optionLabel,
                                                       @Nullable Map<String, Set<String>> controlsValues,
                                                       boolean overwrite) {
        checkNotNull(reportUnitUri, "Report uri should not be null");
        checkNotNull(optionLabel, "Option label should not be null");
        checkNotNull(controlsValues, "Controls values should not be null");

        Call<ReportOption> call = mRestApi.createReportOption(reportUnitUri, optionLabel, controlsValues, overwrite);
        return CallWrapper.wrap(call).body();
    }

    @Override
    public void updateReportOption(@Nullable String reportUnitUri,
                                               @Nullable String optionId,
                                               @Nullable Map<String, Set<String>> controlsValues) {
        checkNotNull(reportUnitUri, "Report uri should not be null");
        checkNotNull(optionId, "Option id should not be null");
        checkNotNull(controlsValues, "Controls values should not be null");

        Call<Response> call = mRestApi.updateReportOption(reportUnitUri, optionId, controlsValues);
        CallWrapper.wrap(call).body();
    }

    @Override
    public void deleteReportOption(@Nullable String reportUnitUri,
                                               @Nullable String optionId) {
        checkNotNull(reportUnitUri, "Report uri should not be null");
        checkNotNull(optionId, "Option id should not be null");

        Call<Response> call = mRestApi.deleteReportOption(reportUnitUri, optionId);
        CallWrapper.wrap(call).body();
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitUri}/options")
        Call<ReportOptionSet> requestReportOptionsList(
                @NonNull @Path(value = "reportUnitUri", encoded = true) String reportUnitUri);

        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reports{reportUnitURI}/options")
        Call<ReportOption> createReportOption(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NonNull @Query("label") String optionLabel,
                @NonNull @Body Map<String, Set<String>> controlsValues,
                @Query("overwrite") boolean overwrite);

        @NonNull
        @Headers("Accept: application/json")
        @PUT("rest_v2/reports{reportUnitURI}/options/{optionId}")
        Call<com.squareup.okhttp.Response> updateReportOption(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NonNull @Path(value = "optionId", encoded = true) String optionId,
                @NonNull @Body Map<String, Set<String>> controlsValues);

        @NonNull
        @Headers("Accept: application/json")
        @DELETE("rest_v2/reports{reportUnitURI}/options/{optionId}")
        Call<com.squareup.okhttp.Response> deleteReportOption(
                @NonNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NonNull @Path(value = "optionId", encoded = true) String optionId);
    }
}
