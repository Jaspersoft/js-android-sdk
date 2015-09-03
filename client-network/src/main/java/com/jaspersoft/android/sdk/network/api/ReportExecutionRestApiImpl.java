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

import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.execution.ReportParameter;

import java.io.IOException;
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
    public Call<ReportExecutionDetailsResponse> runReportExecution(@NonNull ReportExecutionRequestOptions executionOptions) {
        return mRestApi.runReportExecution(executionOptions);
    }

    @NonNull
    @Override
    public Call<ReportExecutionDetailsResponse> requestReportExecutionDetails(@NonNull String executionId) {
        return mRestApi.requestReportExecutionDetails(executionId);
    }

    @NonNull
    @Override
    public Call<ExecutionStatusResponse> requestReportExecutionStatus(@NonNull String executionId) {
        return mRestApi.requestReportExecutionStatus(executionId);
    }

    @Override
    public boolean cancelReportExecution(@NonNull String executionId) {
        Call<?> call = mRestApi.cancelReportExecution(executionId, ExecutionStatusResponse.cancelledStatus());
        // TODO in order to wrap response we need use CallAdapter approach
        try {
            Response<?> response = call.execute();
            int status = response.code();
            return status != 204;
        } catch (IOException e) {
            // We need to wrap response in call. For now we will rethrow error
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateReportExecution(@NonNull String executionId, @NonNull Collection<ReportParameter> params) {
        Call<?> call = mRestApi.updateReportExecution(executionId, params);
        Response<?> response = null;
        // TODO in order to wrap response we need use CallAdapter approach
        try {
            response = call.execute();
            int status = response.code();
            return status == 204;
        } catch (IOException e) {
            // We need to wrap response in call. For now we will rethrow error
            throw new RuntimeException(e);
        }
    }

    @NonNull
    @Override
    public Call<ReportExecutionSearchResponse> searchReportExecution(Map<String, String> params) {
        return mRestApi.searchReportExecution(params);

// TODO we need to use CallAdapater in order to wrap our response in call object
//        int status = response.getStatus();
//        if (status == 204) {
//            return ReportExecutionSearchResponse.empty();
//        } else {
//            ResponseEntity<ReportExecutionSearchResponse> responseEntity =
//                    mRestAdapterWrapper.produce(response, ReportExecutionSearchResponse.class);
//            return responseEntity.getEntity();
//        }
    }

    interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("/rest_v2/reportExecutions")
        Call<ReportExecutionDetailsResponse> runReportExecution(@NonNull @Body ReportExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}")
        Call<ReportExecutionDetailsResponse> requestReportExecutionDetails(@NonNull @Path(value = "executionId", encoded = false) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}/status")
        Call<ExecutionStatusResponse> requestReportExecutionStatus(@NonNull @Path(value = "executionId", encoded = false) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @POST("/rest_v2/reportExecutions/{executionId}/parameters")
        Call<?> updateReportExecution(@NonNull @Path(value = "executionId", encoded = false) String executionId,
                                       @NonNull @Body Collection<ReportParameter> params);

        @NonNull
        @Headers("Accept: application/json")
        @PUT("/rest_v2/reportExecutions/{executionId}/status")
        Call<?> cancelReportExecution(@NonNull @Path(value = "executionId", encoded = false) String executionId,
                                       @NonNull @Body ExecutionStatusResponse statusResponse);

        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions")
        Call<ReportExecutionSearchResponse> searchReportExecution(@Nullable @QueryMap(encoded = false) Map<String, String> params);
    }
}
