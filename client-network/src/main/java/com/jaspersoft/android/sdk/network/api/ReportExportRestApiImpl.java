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
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.entity.export.ExportInput;
import com.jaspersoft.android.sdk.network.entity.export.ExportResourceResponse;
import com.jaspersoft.android.sdk.network.entity.export.ReportExportExecutionResponse;
import com.squareup.okhttp.ResponseBody;

import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;
import rx.functions.Func1;

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
    public Observable<ReportExportExecutionResponse> runExportExecution(@Nullable String executionId,
                                                                        @Nullable ExecutionRequestOptions executionOptions) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(executionOptions, "Execution options should not be null");

        return mRestApi.runReportExportExecution(executionId, executionOptions)
                .onErrorResumeNext(RestErrorAdapter.<ReportExportExecutionResponse>get());
    }

    @NonNull
    @Override
    public Observable<ExecutionStatusResponse> checkExportExecutionStatus(@Nullable String executionId, @Nullable String exportId) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(exportId, "Export id should not be null");

        return mRestApi.checkReportExportStatus(executionId, exportId)
                .onErrorResumeNext(RestErrorAdapter.<ExecutionStatusResponse>get());
    }

    @NonNull
    @Override
    public Observable<ExportResourceResponse> requestExportOutput(@Nullable String executionId, @Nullable String exportId) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(exportId, "Export id should not be null");

        return mRestApi.requestReportExportOutput(executionId, exportId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<ExportResourceResponse>>() {
                    @Override
                    public Observable<ExportResourceResponse> call(Response<ResponseBody> rawResponse) {
                        com.squareup.okhttp.Headers headers = rawResponse.headers();

                        RetrofitExportInput exportInput = new RetrofitExportInput(rawResponse.body());
                        String pages = headers.get("report-pages");
                        boolean isFinal = Boolean.parseBoolean(headers.get("output-final"));

                        ExportResourceResponse response = ExportResourceResponse.create(exportInput, pages, isFinal);
                        return Observable.just(response);
                    }
                })
                .onErrorResumeNext(RestErrorAdapter.<ExportResourceResponse>get());
    }

    @NonNull
    @Override
    public Observable<ExportInput> requestExportAttachment(@Nullable String executionId, @Nullable String exportId, @Nullable String attachmentId) {
        checkNotNull(executionId, "Execution id should not be null");
        checkNotNull(exportId, "Export id should not be null");
        checkNotNull(attachmentId, "Attachment id should not be null");

        return mRestApi.requestReportExportAttachmentOutput(executionId, exportId, attachmentId)
                .flatMap(new Func1<Response<ResponseBody>, Observable<ExportInput>>() {
                    @Override
                    public Observable<ExportInput> call(Response<ResponseBody> rawResponse) {
                        ResponseBody body = rawResponse.body();
                        ExportInput response = new RetrofitExportInput(body);
                        return Observable.just(response);
                    }
                })
                .onErrorResumeNext(RestErrorAdapter.<ExportInput>get());
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("rest_v2/reportExecutions/{executionId}/exports")
        Observable<ReportExportExecutionResponse> runReportExportExecution(@NonNull @Path("executionId") String executionId,
                                                                           @NonNull @Body ExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/status")
        Observable<ExecutionStatusResponse> checkReportExportStatus(@NonNull @Path("executionId") String executionId,
                                                                    @NonNull @Path("exportId") String exportId);

        /**
         * 'suppressContentDisposition' used due to security implications this header has
         */
        @NonNull
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/outputResource?suppressContentDisposition=true")
        Observable<Response<ResponseBody>> requestReportExportOutput(@NonNull @Path("executionId") String executionId,
                                                                     @NonNull @Path("exportId") String exportId);

        @NonNull
        @GET("rest_v2/reportExecutions/{executionId}/exports/{exportId}/attachments/{attachmentId}")
        Observable<Response<ResponseBody>> requestReportExportAttachmentOutput(@NonNull @Path("executionId") String executionId,
                                                                               @NonNull @Path("exportId") String exportId,
                                                                               @NonNull @Path("attachmentId") String attachmentId);
    }
}
