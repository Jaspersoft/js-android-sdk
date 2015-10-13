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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

import static com.jaspersoft.android.sdk.network.api.Utils.checkNotNull;

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
    public ExportExecutionDescriptor runExportExecution(@Nullable String executionId,
                                                            @Nullable ExecutionRequestOptions executionOptions) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(executionOptions, "Execution options should not be null");

        Call<ExportExecutionDescriptor> call = mRestApi.runReportExportExecution(executionId, executionOptions);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public ExecutionStatus checkExportExecutionStatus(@Nullable String executionId, @Nullable String exportId) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(exportId, "Export id should not be null");

        Call<ExecutionStatus> call = mRestApi.checkReportExportStatus(executionId, exportId);
        return CallWrapper.wrap(call).body();
    }

    @NonNull
    @Override
    public ExportOutputResource requestExportOutput(@Nullable String executionId, @Nullable String exportId) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(exportId, "Export id should not be null");

        Call<ResponseBody> call = mRestApi.requestReportExportOutput(executionId, exportId);
        Response<ResponseBody> rawResponse = CallWrapper.wrap(call).response();
        com.squareup.okhttp.Headers headers = rawResponse.headers();

        RetrofitOutputResource exportInput = new RetrofitOutputResource(rawResponse.body());
        String pages = headers.get("report-pages");
        boolean isFinal = Boolean.parseBoolean(headers.get("output-final"));

        return ExportOutputResource.create(exportInput, pages, isFinal);
    }

    @NonNull
    @Override
    public OutputResource requestExportAttachment(@Nullable String executionId, @Nullable String exportId, @Nullable String attachmentId) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(exportId, "Export id should not be null");
        checkNotNull(attachmentId, "Attachment id should not be null");

        Call<ResponseBody> call = mRestApi.requestReportExportAttachmentOutput(executionId, exportId, attachmentId);
        Response<ResponseBody> rawResponse = CallWrapper.wrap(call).response();
        ResponseBody body = rawResponse.body();
        return new RetrofitOutputResource(body);
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reportExecutions/{executionId}/exports")
        Call<ExportExecutionDescriptor> runReportExportExecution(@NonNull @Path("executionId") String executionId,
                                                                     @NonNull @Body ExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/status")
        Call<ExecutionStatus> checkReportExportStatus(@NonNull @Path("executionId") String executionId,
                                                              @NonNull @Path("exportId") String exportId);

        /**
         * 'suppressContentDisposition' used due to security implications this header has
         */
        @NonNull
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/outputResource?suppressContentDisposition=true")
        Call<ResponseBody> requestReportExportOutput(@NonNull @Path("executionId") String executionId,
                                                     @NonNull @Path("exportId") String exportId);

        @NonNull
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/attachments/{attachmentId}")
        Call<ResponseBody> requestReportExportAttachmentOutput(@NonNull @Path("executionId") String executionId,
                                                               @NonNull @Path("exportId") String exportId,
                                                               @NonNull @Path("attachmentId") String attachmentId);
    }
}
