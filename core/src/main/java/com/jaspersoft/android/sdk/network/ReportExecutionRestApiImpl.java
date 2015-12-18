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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExecutionRestApiImpl implements ReportExecutionRestApi {

    private final RestApi mRestApi;

    ReportExecutionRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NotNull
    @Override
    public ReportExecutionDescriptor runReportExecution(@Nullable Cookies cookies,
                                                        @Nullable ReportExecutionRequestOptions executionOptions) throws IOException, HttpException {
        Utils.checkNotNull(executionOptions, "Execution options should not be null");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        Call<ReportExecutionDescriptor> call = mRestApi.runReportExecution(executionOptions, cookies.toString());
        return CallWrapper.wrap(call).body();
    }

    @NotNull
    @Override
    public ReportExecutionDescriptor requestReportExecutionDetails(@Nullable Cookies cookies,
                                                                   @Nullable String executionId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        Call<ReportExecutionDescriptor> call = mRestApi.requestReportExecutionDetails(executionId, cookies.toString());
        return CallWrapper.wrap(call).body();
    }

    @NotNull
    @Override
    public ExecutionStatus requestReportExecutionStatus(@Nullable Cookies cookies,
                                                        @Nullable String executionId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        Call<ExecutionStatus> call = mRestApi.requestReportExecutionStatus(executionId, cookies.toString());
        return CallWrapper.wrap(call).body();
    }

    @Override
    public boolean cancelReportExecution(@Nullable Cookies cookies,
                                         @Nullable String executionId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        Call<Object> call = mRestApi.cancelReportExecution(executionId, ExecutionStatus.cancelledStatus(), cookies.toString());
        Response<Object> response = CallWrapper.wrap(call).response();
        int status = response.code();
        return status != 204;
    }

    @Override
    public boolean updateReportExecution(@Nullable Cookies cookies,
                                         @Nullable String executionId,
                                         @Nullable List<ReportParameter> params) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(params, "Execution params should not be null");
        Utils.checkArgument(params.isEmpty(), "Execution params should not be empty");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        Call<Object> call = mRestApi.updateReportExecution(executionId, params, cookies.toString());
        Response<Object> response = CallWrapper.wrap(call).response();
        int status = response.code();
        return status == 204;
    }

    @NotNull
    @Override
    public ReportExecutionSearchResponse searchReportExecution(@Nullable Cookies cookies,
                                                               @Nullable Map<String, String> params) throws IOException, HttpException {
        Utils.checkNotNull(params, "Search params should not be null");
        Utils.checkArgument(params.isEmpty(), "Search params should have at lease one key pair");
        Utils.checkNotNull(cookies, "Request cookies should not be null");

        Call<ReportExecutionSearchResponse> call = mRestApi.searchReportExecution(params, cookies.toString());
        ReportExecutionSearchResponse body = CallWrapper.wrap(call).body();
        if (body == null) {
            return ReportExecutionSearchResponse.empty();
        }
        return body;
    }

    interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reportExecutions")
        Call<ReportExecutionDescriptor> runReportExecution(@NotNull @Body ReportExecutionRequestOptions executionOptions,
                                                           @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}")
        Call<ReportExecutionDescriptor> requestReportExecutionDetails(@NotNull @Path(value = "executionId", encoded = true) String executionId,
                                                                      @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}/status")
        Call<ExecutionStatus> requestReportExecutionStatus(@NotNull @Path(value = "executionId", encoded = true) String executionId,
                                                           @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reportExecutions/{executionId}/parameters")
        Call<Object> updateReportExecution(@NotNull @Path(value = "executionId", encoded = true) String executionId,
                                           @NotNull @Body List<ReportParameter> params,
                                           @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @PUT("rest_v2/reportExecutions/{executionId}/status")
        Call<Object> cancelReportExecution(@NotNull @Path(value = "executionId", encoded = true) String executionId,
                                           @NotNull @Body ExecutionStatus statusResponse,
                                           @Header("Cookie") String cookie);

        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions")
        Call<ReportExecutionSearchResponse> searchReportExecution(@Nullable @QueryMap(encoded = true) Map<String, String> params,
                                                                  @Header("Cookie") String cookie);
    }
}
