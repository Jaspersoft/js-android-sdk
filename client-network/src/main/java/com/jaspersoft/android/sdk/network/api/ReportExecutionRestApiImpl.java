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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportParameter;

import java.util.Collection;
import java.util.Map;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

import static com.jaspersoft.android.sdk.network.api.Utils.checkArgument;
import static com.jaspersoft.android.sdk.network.api.Utils.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExecutionRestApiImpl implements ReportExecutionRestApi {

    private final RestApi mRestApi;

    ReportExecutionRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public ReportExecutionDetailsResponse runReportExecution(@Nullable ReportExecutionRequestOptions executionOptions) {
        checkNotNull(executionOptions, "Execution options should not be null");

        Call<ReportExecutionDetailsResponse> call = mRestApi.runReportExecution(executionOptions);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public ReportExecutionDetailsResponse requestReportExecutionDetails(@Nullable String executionId) {
        checkNotNull(executionId, "Execution id should not be null");

        Call<ReportExecutionDetailsResponse> call = mRestApi.requestReportExecutionDetails(executionId);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public ExecutionStatusResponse requestReportExecutionStatus(@Nullable String executionId) {
        checkNotNull(executionId, "Execution id should not be null");

        Call<ExecutionStatusResponse> call = mRestApi.requestReportExecutionStatus(executionId);
        return CallWrapper.wrap(call).body();
    }

    @Override
    public boolean cancelReportExecution(@Nullable String executionId) {
        checkNotNull(executionId, "Execution id should not be null");

        Call<Object> call = mRestApi.cancelReportExecution(executionId, ExecutionStatusResponse.cancelledStatus());
        Response<Object> response = CallWrapper.wrap(call).response();
        int status = response.code();
        return status != 204;
    }

    @Override
    public boolean updateReportExecution(@Nullable String executionId, @Nullable Collection<ReportParameter> params) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(params, "Execution params id should not be null");

        Call<Object> call =  mRestApi.updateReportExecution(executionId, params);
        Response<Object> response = CallWrapper.wrap(call).response();
        int status = response.code();
        return status == 204;
    }

    @NonNull
    @Override
    public ReportExecutionSearchResponse searchReportExecution(@Nullable Map<String, String> params) {
        checkNotNull(params, "Search params should not be null");
        checkArgument(params.isEmpty(), "Search params should have at lease one key pair");

        Call<ReportExecutionSearchResponse> call = mRestApi.searchReportExecution(params);
        ReportExecutionSearchResponse body = CallWrapper.wrap(call).body();
        if (body == null) {
            return ReportExecutionSearchResponse.empty();
        }
        return body;
    }

    interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reportExecutions")
        Call<ReportExecutionDetailsResponse> runReportExecution(@NonNull @Body ReportExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}")
        Call<ReportExecutionDetailsResponse> requestReportExecutionDetails(@NonNull @Path(value = "executionId", encoded = true) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}/status")
        Call<ExecutionStatusResponse> requestReportExecutionStatus(@NonNull @Path(value = "executionId", encoded = true) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reportExecutions/{executionId}/parameters")
        Call<Object> updateReportExecution(@NonNull @Path(value = "executionId", encoded = true) String executionId,
                                                           @NonNull @Body Collection<ReportParameter> params);

        @NonNull
        @Headers("Accept: application/json")
        @PUT("rest_v2/reportExecutions/{executionId}/status")
        Call<Object> cancelReportExecution(@NonNull @Path(value = "executionId", encoded = true) String executionId,
                                                           @NonNull @Body ExecutionStatusResponse statusResponse);

        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions")
        Call<ReportExecutionSearchResponse> searchReportExecution(@Nullable @QueryMap(encoded = true) Map<String, String> params);
    }
}
