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
import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ExecutionStatusResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.export.ExportInput;
import com.jaspersoft.android.sdk.network.rest.v2.entity.export.ExportResourceResponse;
import com.jaspersoft.android.sdk.network.rest.v2.entity.export.ReportExportExecutionResponse;

import retrofit.RestAdapter;
import retrofit.client.Response;
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

    public ReportExportRestApiImpl(RestAdapter restAdapter) {
        mRestApi = restAdapter.create(RestApi.class);
    }

    @NonNull
    @Override
    public ReportExportExecutionResponse runExecution(@NonNull String executionId,
                                                      @NonNull ExecutionRequestOptions executionOptions) {
        return mRestApi.runReportExportExecution(executionId, executionOptions);
    }

    @NonNull
    @Override
    public ExecutionStatusResponse checkExecutionStatus(@NonNull String executionId, @NonNull String exportId) {
        return mRestApi.checkReportExportStatus(executionId, exportId);
    }

    @NonNull
    @Override
    public ExportResourceResponse requestOutput(@NonNull String executionId, @NonNull String exportId) {
        Response response = mRestApi.requestReportExportOutput(executionId, exportId);
        RetrofitExportInput exportInput = new RetrofitExportInput(response.getBody());
        HeaderUtil headerUtil = HeaderUtil.wrap(response);
        String pages = headerUtil.getFirstHeader("report-pages").asString();
        boolean isFinal = headerUtil.getFirstHeader("output-final").asBoolean();
        return ExportResourceResponse.create(exportInput, pages, isFinal);
    }

    @NonNull
    @Override
    public ExportInput requestAttachment(@NonNull String executionId, @NonNull String exportId, @NonNull String attachmentId) {
        Response response = mRestApi.requestReportExportAttachmentOutput(executionId, exportId, attachmentId);
        ExportInput input = new RetrofitExportInput(response.getBody());
        return input;
    }

    private interface RestApi {
        @NonNull
        @Headers("Accept: application/json")
        @POST("/rest_v2/reportExecutions/{executionId}/exports")
        ReportExportExecutionResponse runReportExportExecution(@NonNull @Path("executionId") String executionId,
                                                               @NonNull @Body ExecutionRequestOptions executionOptions);

        @NonNull
        @Headers("Accept: application/json")
        @GET("/rest_v2/reportExecutions/{executionId}/exports/{exportId}/status")
        ExecutionStatusResponse checkReportExportStatus(@NonNull @Path("executionId") String executionId,
                                                        @NonNull @Path("exportId") String exportId);

        /**
         * 'suppressContentDisposition' used due to security implications this header has
         */
        @NonNull
        @GET("/rest_v2/reportExecutions/{executionId}/exports/{exportId}/outputResource?suppressContentDisposition=true")
        Response requestReportExportOutput(@NonNull @Path("executionId") String executionId,
                                           @NonNull @Path("exportId") String exportId);

        @NonNull
        @GET("/rest_v2/reportExecutions/{executionId}/exports/{exportId}/attachments/{attachmentId}")
        Response requestReportExportAttachmentOutput(@NonNull @Path("executionId") String executionId,
                                           @NonNull @Path("exportId") String exportId,
                                           @NonNull @Path("attachmentId") String attachmentId);
    }
}