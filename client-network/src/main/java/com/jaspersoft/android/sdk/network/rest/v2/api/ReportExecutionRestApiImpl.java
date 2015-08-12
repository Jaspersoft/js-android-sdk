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

package com.jaspersoft.android.sdk.network.rest.v2.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ReportParameter;

import java.util.Collection;
import java.util.Map;

import retrofit.ResponseEntity;
import retrofit.RestAdapter;
import retrofit.RestAdapterWrapper;
import retrofit.client.Response;
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
    private final RestAdapterWrapper mRestAdapterWrapper;

    ReportExecutionRestApiImpl(RestAdapter restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
        mRestAdapterWrapper = RestAdapterWrapper.wrap(restAdapter);
    }

    @NonNull
    @Override
    public ReportExecutionDetailsResponse runReportExecution(@NonNull ReportExecutionRequestOptions executionOptions) {
        return mRestApi.runReportExecution(executionOptions);
    }

    @NonNull
    @Override
    public ReportExecutionDetailsResponse requestReportExecutionDetails(@NonNull String executionId) {
        return mRestApi.requestReportExecutionDetails(executionId);
    }

    @NonNull
    @Override
    public ExecutionStatusResponse requestReportExecutionStatus(@NonNull String executionId) {
        return mRestApi.requestReportExecutionStatus(executionId);
    }

    @Override
    public boolean cancelReportExecution(@NonNull String executionId) {
        Response response = mRestApi.cancelReportExecution(executionId, ExecutionStatusResponse.cancelledStatus());
        int status = response.getStatus();
        return status != 204;
    }

    @Override
    public boolean updateReportExecution(@NonNull String executionId, @NonNull Collection<ReportParameter> params) {
        Response response = mRestApi.updateReportExecution(executionId, params);
        int status = response.getStatus();
        return status == 204;
    }

    @NonNull
    @Override
    public ReportExecutionSearchResponse searchReportExecution(Map<String, String> params) {
        Response response = mRestApi.searchReportExecution(params);
        int status = response.getStatus();
        if (status == 204) {
            return ReportExecutionSearchResponse.empty();
        } else {
            ResponseEntity<ReportExecutionSearchResponse> responseEntity =
                    mRestAdapterWrapper.produce(response, ReportExecutionSearchResponse.class);
            return responseEntity.getEntity();
        }
    }

    interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("/rest_v2/reportExecutions")
        ReportExecutionDetailsResponse runReportExecution(@NonNull @Body ReportExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}")
        ReportExecutionDetailsResponse requestReportExecutionDetails(@NonNull @Path(value = "executionId", encode = false) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}/status")
        ExecutionStatusResponse requestReportExecutionStatus(@NonNull @Path(value = "executionId", encode = false) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @POST("/rest_v2/reportExecutions/{executionId}/parameters")
        Response updateReportExecution(@NonNull @Path(value = "executionId", encode = false) String executionId,
                                       @NonNull @Body Collection<ReportParameter> params);

        @NonNull
        @Headers("Accept: application/json")
        @PUT("/rest_v2/reportExecutions/{executionId}/status")
        Response cancelReportExecution(@NonNull @Path(value = "executionId", encode = false) String executionId,
                                       @NonNull @Body ExecutionStatusResponse statusResponse);

        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions")
        Response searchReportExecution(@Nullable @QueryMap(encodeValues = false) Map<String, String> params);
    }
}
