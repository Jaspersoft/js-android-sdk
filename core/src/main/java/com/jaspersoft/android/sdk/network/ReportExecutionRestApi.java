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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExecutionRestApi {

    private final NetworkClient mNetworkClient;

    ReportExecutionRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    @NotNull
    public ReportExecutionDescriptor runReportExecution(@Nullable ReportExecutionRequestOptions executionOptions) throws IOException, HttpException {
        Utils.checkNotNull(executionOptions, "Execution options should not be null");

        HttpUrl url = mNetworkClient.getBaseUrl().resolve("rest_v2/reportExecutions");

        RequestBody jsonRequestBody = mNetworkClient.createJsonRequestBody(executionOptions);
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .post(jsonRequestBody)
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, ReportExecutionDescriptor.class);
    }

    @NotNull
    public ReportExecutionDescriptor requestReportExecutionDetails(@Nullable String executionId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");

        HttpUrl url = mNetworkClient.getBaseUrl().resolve("rest_v2/reportExecutions")
                .newBuilder()
                .addPathSegment(executionId)
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, ReportExecutionDescriptor.class);
    }

    @NotNull
    public ExecutionStatus requestReportExecutionStatus(@Nullable String executionId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");

        HttpUrl url = mNetworkClient.getBaseUrl()
                .resolve("rest_v2/reportExecutions")
                .newBuilder()
                .addPathSegment(executionId)
                .addPathSegment("status")
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, ExecutionStatus.class);
    }

    public boolean cancelReportExecution(@Nullable String executionId) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");

        HttpUrl url = mNetworkClient.getBaseUrl()
                .resolve("rest_v2/reportExecutions")
                .newBuilder()
                .addPathSegment(executionId)
                .addPathSegment("status")
                .build();

        RequestBody jsonRequestBody = mNetworkClient.createJsonRequestBody(ExecutionStatus.cancelledStatus());
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .put(jsonRequestBody)
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        int status = response.code();
        return status != 204;
    }

    public boolean updateReportExecution(@Nullable String executionId,
                                         @Nullable List<ReportParameter> params) throws IOException, HttpException {
        Utils.checkNotNull(executionId, "Execution id should not be null");
        Utils.checkNotNull(params, "Execution params should not be null");
        Utils.checkArgument(params.isEmpty(), "Execution params should not be empty");


        HttpUrl url = mNetworkClient.getBaseUrl()
                .resolve("rest_v2/reportExecutions")
                .newBuilder()
                .addPathSegment(executionId)
                .addPathSegment("parameters")
                .build();

        RequestBody jsonRequestBody = mNetworkClient.createJsonRequestBody(params);
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .post(jsonRequestBody)
                .url(url)
                .build();

        com.squareup.okhttp.Response response = mNetworkClient.makeCall(request);
        int status = response.code();
        return status == 204;
    }
}
