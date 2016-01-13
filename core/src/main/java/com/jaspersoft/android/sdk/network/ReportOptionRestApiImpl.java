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

import com.google.gson.JsonSyntaxException;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionSet;
import com.squareup.okhttp.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportOptionRestApiImpl implements ReportOptionRestApi {
    private final RestApi mRestApi;

    ReportOptionRestApiImpl(Retrofit retrofit) {
        mRestApi = retrofit.create(RestApi.class);
    }

    @NotNull
    @Override
    public Set<ReportOption> requestReportOptionsList(
                                                      @Nullable String reportUnitUri) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");

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

    @NotNull
    @Override
    public ReportOption createReportOption(
                                           @Nullable String reportUnitUri,
                                           @Nullable String optionLabel,
                                           @Nullable List<ReportParameter> parameters,
                                           boolean overwrite) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");
        Utils.checkNotNull(optionLabel, "Option label should not be null");
        Utils.checkNotNull(parameters, "Parameters values should not be null");

        Map<String, Set<String>> controlsValues = ReportParamsMapper.INSTANCE.toMap(parameters);
        Call<ReportOption> call = mRestApi.createReportOption(reportUnitUri, optionLabel, controlsValues, overwrite);
        return CallWrapper.wrap(call).body();
    }

    @Override
    public void updateReportOption(@Nullable String reportUnitUri,
                                   @Nullable String optionId,
                                   @Nullable List<ReportParameter> parameters) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");
        Utils.checkNotNull(optionId, "Option id should not be null");
        Utils.checkNotNull(parameters, "Parameters values should not be null");

        Map<String, Set<String>> controlsValues = ReportParamsMapper.INSTANCE.toMap(parameters);
        Call<Response> call = mRestApi.updateReportOption(reportUnitUri, optionId, controlsValues);
        CallWrapper.wrap(call).body();
    }

    @Override
    public void deleteReportOption(@Nullable String reportUnitUri,
                                   @Nullable String optionId) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");
        Utils.checkNotNull(optionId, "Option id should not be null");

        Call<Response> call = mRestApi.deleteReportOption(reportUnitUri, optionId);
        CallWrapper.wrap(call).body();
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reports{reportUnitUri}/options")
        Call<ReportOptionSet> requestReportOptionsList(
                @NotNull @Path(value = "reportUnitUri", encoded = true) String reportUnitUri);

        @NotNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reports{reportUnitURI}/options")
        Call<ReportOption> createReportOption(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NotNull @Query("label") String optionLabel,
                @NotNull @Body Map<String, Set<String>> controlsValues,
                @Query("overwrite") boolean overwrite);

        @NotNull
        @Headers("Accept: application/json")
        @PUT("rest_v2/reports{reportUnitURI}/options/{optionId}")
        Call<com.squareup.okhttp.Response> updateReportOption(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NotNull @Path(value = "optionId", encoded = true) String optionId,
                @NotNull @Body Map<String, Set<String>> controlsValues);

        @NotNull
        @Headers("Accept: application/json")
        @DELETE("rest_v2/reports{reportUnitURI}/options/{optionId}")
        Call<com.squareup.okhttp.Response> deleteReportOption(
                @NotNull @Path(value = "reportUnitURI", encoded = true) String reportUnitUri,
                @NotNull @Path(value = "optionId", encoded = true) String optionI);
    }
}
