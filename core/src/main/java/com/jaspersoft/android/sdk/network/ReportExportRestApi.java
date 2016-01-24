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
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportRestApi {
    private final NetworkClient mNetworkClient;

    public ReportExportRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    @NotNull
    public ExportExecutionDescriptor runExportExecution(@Nullable String executionId,
                                                        @Nullable ExecutionRequestOptions executionOptions) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(executionOptions, "Execution options should not be null");


        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reportExecutions")
                .addPath(executionId)
                .addPath("exports")
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        RequestBody jsonRequestBody = mNetworkClient.createJsonRequestBody(executionOptions);
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .post(jsonRequestBody)
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, ExportExecutionDescriptor.class);
    }

    @NotNull
    public ExecutionStatus checkExportExecutionStatus(@Nullable String executionId,
                                                      @Nullable String exportId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(exportId, "Export id should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reportExecutions")
                .addPath(executionId)
                .addPath("exports")
                .addPath(exportId)
                .addPath("status")
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, ExecutionStatus.class);
    }

    @NotNull
    public ExportOutputResource requestExportOutput(@Nullable String executionId,
                                                    @Nullable String exportId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(exportId, "Export id should not be null");

        /**
         * 'suppressContentDisposition' used due to security implications this header has
         */
        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reportExecutions")
                .addPath(executionId)
                .addPath("exports")
                .addPath(exportId)
                .addPath("outputResource")
                .build()
                .resolve(mNetworkClient.getBaseUrl())
                .newBuilder()
                .addQueryParameter("suppressContentDisposition", "true")
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        com.squareup.okhttp.Headers headers = response.headers();

        RetrofitOutputResource exportInput = new RetrofitOutputResource(response.body());
        String pages = headers.get("report-pages");
        boolean isFinal = Boolean.parseBoolean(headers.get("output-final"));

        return ExportOutputResource.create(exportInput, pages, isFinal);
    }

    @NotNull
    public OutputResource requestExportAttachment(@Nullable String executionId,
                                                  @Nullable String exportId,
                                                  @Nullable String attachmentId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(exportId, "Export id should not be null");
        Utils.checkNotNull(attachmentId, "Attachment id should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reportExecutions")
                .addPath(executionId)
                .addPath("exports")
                .addPath(exportId)
                .addPath("attachments")
                .addPath(attachmentId)
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return new RetrofitOutputResource(response.body());
    }
}