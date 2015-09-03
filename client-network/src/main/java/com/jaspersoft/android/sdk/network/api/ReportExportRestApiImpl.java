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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.export.ExportInput;
import com.jaspersoft.android.sdk.network.entity.export.ExportResourceResponse;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExportRestApiImpl implements ReportExportRestApi {
    private final RestApi mRestApi;

    public ReportExportRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public Call<ReportExportExecutionResponse> runExportExecution(@NonNull String executionId,
                                                                  @NonNull ExecutionRequestOptions executionOptions) {
        return mRestApi.runReportExportExecution(executionId, executionOptions);
    }

    @NonNull
    @Override
    public Call<ExecutionStatusResponse> checkExportExecutionStatus(@NonNull String executionId, @NonNull String exportId) {
        return mRestApi.checkReportExportStatus(executionId, exportId);
    }

    @NonNull
    @Override
    public ExportResourceResponse requestExportOutput(@NonNull String executionId, @NonNull String exportId) {
        Call<ResponseBody> call = mRestApi.requestReportExportOutput(executionId, exportId);
        // TODO in order to wrap response we need use CallAdapter approach
        try {
            Response<ResponseBody> response = call.execute();
            com.squareup.okhttp.Headers headers = response.headers();

            RetrofitExportInput exportInput = new RetrofitExportInput(response.body());
            String pages = headers.get("report-pages");
            boolean isFinal = Boolean.parseBoolean(headers.get("output-final"));

            return ExportResourceResponse.create(exportInput, pages, isFinal);
        } catch (IOException e) {
            // We need to wrap response in call. For now we will rethrow error
            throw new RuntimeException(e);
        }
    }

    @NonNull
    @Override
    public ExportInput requestExportAttachment(@NonNull String executionId, @NonNull String exportId, @NonNull String attachmentId) {
        Call<ResponseBody> call = mRestApi.requestReportExportAttachmentOutput(executionId, exportId, attachmentId);
        // TODO in order to wrap response we need use CallAdapter approach
        try {
            Response<ResponseBody> response = call.execute();
            return new RetrofitExportInput(response.body());
        } catch (IOException e) {
            // We need to wrap response in call. For now we will rethrow error
            throw new RuntimeException(e);
        }
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("/rest_v2/reportExecutions/{executionId}/exports")
        Call<ReportExportExecutionResponse> runReportExportExecution(@NonNull @Path("executionId") String executionId,
                                                               @NonNull @Body ExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}/exports/{exportId}/status")
        Call<ExecutionStatusResponse> checkReportExportStatus(@NonNull @Path("executionId") String executionId,
                                                        @NonNull @Path("exportId") String exportId);

        /**
         * 'suppressContentDisposition' used due to security implications this header has
         */
        @NonNull
        @GET("/rest_v2/reportExecutions/{executionId}/exports/{exportId}/outputResource?suppressContentDisposition=true")
        Call<ResponseBody> requestReportExportOutput(@NonNull @Path("executionId") String executionId,
                                           @NonNull @Path("exportId") String exportId);

        @NonNull
        @GET("/rest_v2/reportExecutions/{executionId}/exports/{exportId}/attachments/{attachmentId}")
        Call<ResponseBody> requestReportExportAttachmentOutput(@NonNull @Path("executionId") String executionId,
                                           @NonNull @Path("exportId") String exportId,
                                           @NonNull @Path("attachmentId") String attachmentId);
    }
}
