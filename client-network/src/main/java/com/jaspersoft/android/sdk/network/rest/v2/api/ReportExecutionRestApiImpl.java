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

import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ReportExecutionDetailsResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ReportExecutionStatusResponse;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExecutionRestApiImpl implements ReportExecutionRestApi {

    private final RestApi mRestApi;

    ReportExecutionRestApiImpl(RestAdapter restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public ReportExecutionDetailsResponse runReportExecution(@NonNull ExecutionRequestOptions executionOptions) {
        return mRestApi.runReportExecution(executionOptions);
    }

    @NonNull
    @Override
    public ReportExecutionDetailsResponse requestReportExecutionDetails(@NonNull String executionId) {
        return mRestApi.requestReportExecutionDetails(executionId);
    }

    @NonNull
    @Override
    public ReportExecutionStatusResponse requestReportExecutionStatus(@NonNull String executionId) {
        return mRestApi.requestReportExecutionStatus(executionId);
    }

    @Override
    public boolean cancelReportExecution(@NonNull String executionId) {
        Response response = mRestApi.cancelReportExecution(executionId, ReportExecutionStatusResponse.cancelledStatus());
        int status = response.getStatus();
        return status != 204;
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("/rest_v2/reportExecutions")
        ReportExecutionDetailsResponse runReportExecution(@NonNull @Body ExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}")
        ReportExecutionDetailsResponse requestReportExecutionDetails(@NonNull @Path(value = "executionId", encode = false) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}/status")
        ReportExecutionStatusResponse requestReportExecutionStatus(@NonNull @Path(value = "executionId", encode = false) String executionId);

        @NonNull
        @Headers("Accept: application/json")
        @PUT("/rest_v2/reportExecutions/{executionId}/status")
        Response cancelReportExecution(@NonNull @Path(value = "executionId", encode = false) String executionId,
                                       @NonNull @Body ReportExecutionStatusResponse statusResponse);
    }
}
