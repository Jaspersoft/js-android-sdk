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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.squareup.okhttp.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.*;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ReportExportRestApiImpl implements ReportExportRestApi {
    private final RestApi mRestApi;

    public ReportExportRestApiImpl(Retrofit restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NotNull
    @Override
    public ExportExecutionDescriptor runExportExecution(@Nullable String token,
                                                        @Nullable String executionId,
                                                        @Nullable ExecutionRequestOptions executionOptions) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(executionOptions, "Execution options should not be null");
        Utils.checkNotNull(token, "Request token should not be null");

        Call<ExportExecutionDescriptor> call = mRestApi.runReportExportExecution(executionId, executionOptions, token);
        return CallWrapper.wrap(call).body();
    }

    @NotNull
    @Override
    public ExecutionStatus checkExportExecutionStatus(@Nullable String token,
                                                      @Nullable String executionId,
                                                      @Nullable String exportId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(exportId, "Export id should not be null");
        Utils.checkNotNull(token, "Request token should not be null");

        Call<ExecutionStatus> call = mRestApi.checkReportExportStatus(executionId, exportId, token);
        return CallWrapper.wrap(call).body();
    }

    @NotNull
    @Override
    public ExportOutputResource requestExportOutput(@Nullable String token,
                                                    @Nullable String executionId,
                                                    @Nullable String exportId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(exportId, "Export id should not be null");
        Utils.checkNotNull(token, "Request token should not be null");

        Call<ResponseBody> call = mRestApi.requestReportExportOutput(executionId, exportId, token);
        Response<ResponseBody> rawResponse = CallWrapper.wrap(call).response();
        com.squareup.okhttp.Headers headers = rawResponse.headers();

        RetrofitOutputResource exportInput = new RetrofitOutputResource(rawResponse.body());
        String pages = headers.get("report-pages");
        boolean isFinal = Boolean.parseBoolean(headers.get("output-final"));

        return ExportOutputResource.create(exportInput, pages, isFinal);
    }

    @NotNull
    @Override
    public OutputResource requestExportAttachment(@Nullable String token,
                                                  @Nullable String executionId,
                                                  @Nullable String exportId,
                                                  @Nullable String attachmentId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(exportId, "Export id should not be null");
        Utils.checkNotNull(attachmentId, "Attachment id should not be null");
        Utils.checkNotNull(token, "Request token should not be null");

        Call<ResponseBody> call = mRestApi.requestReportExportAttachmentOutput(executionId, exportId, attachmentId, token);
        Response<ResponseBody> rawResponse = CallWrapper.wrap(call).response();
        ResponseBody body = rawResponse.body();
        return new RetrofitOutputResource(body);
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reportExecutions/{executionId}/exports")
        Call<ExportExecutionDescriptor> runReportExportExecution(@NotNull @Path("executionId") String executionId,
                                                                 @NotNull @Body ExecutionRequestOptions executionOptions,
                                                                 @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/status")
        Call<ExecutionStatus> checkReportExportStatus(@NotNull @Path("executionId") String executionId,
                                                      @NotNull @Path(value = "exportId", encoded = true) String exportId,
                                                      @Header("Cookie") String cookie);

        /**
         * 'suppressContentDisposition' used due to security implications this header has
         */
        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/outputResource?suppressContentDisposition=true")
        Call<ResponseBody> requestReportExportOutput(@NotNull @Path("executionId") String executionId,
                                                     @NotNull @Path(value = "exportId", encoded = true) String exportId,
                                                     @Header("Cookie") String cookie);

        @NotNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/attachments/{attachmentId}")
        Call<ResponseBody> requestReportExportAttachmentOutput(@NotNull @Path("executionId") String executionId,
                                                               @NotNull @Path(value = "exportId", encoded = true) String exportId,
                                                               @NotNull @Path("attachmentId") String attachmentId,
                                                               @Header("Cookie") String cookie);
    }
}
