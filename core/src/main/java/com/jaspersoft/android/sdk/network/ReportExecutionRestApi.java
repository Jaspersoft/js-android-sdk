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

import java.io.IOException;
import java.util.List;

/**
 * Public API that allows to initiate/update/cancel report execution and requests both details and state
 *
 * <pre>
 * {@code
 *
 *   Server server = Server.builder()
 *       .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *       .build();
 *
 *   Credentials credentials = SpringCredentials.builder()
 *       .withPassword("phoneuser")
 *       .withUsername("phoneuser")
 *       .withOrganization("organization_1")
 *       .build();
 *
 *
 *   AuthorizedClient client = server.newClient(credentials)
 *       .create();
 *   ReportExecutionRestApi reportExecutionRestApi = client.reportExecutionApi();
 *   ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest("/report/uri");
 *
 *   try {
 *       // Initial details of execution
 *       ReportExecutionDescriptor initialDetails = reportExecutionRestApi.runReportExecution(options);
 *       String executionId = initialDetails.getExecutionId();
 *
 *       boolean cancelled = reportExecutionRestApi.cancelReportExecution(executionId);
 *
 *       List<ReportParameter> params = Collections.singletonList(
 *          new ReportParameter("key", Collections.singleton("value"))
 *       );
 *       boolean updated = reportExecutionRestApi.updateReportExecution(executionId, params);
 *
 *       ReportExecutionDescriptor details = reportExecutionRestApi.requestReportExecutionDetails(executionId);
 *
 *       ExecutionStatus executionStatus = reportExecutionRestApi.requestReportExecutionStatus(executionId);
 *   } catch (IOException e) {
 *       // handle socket issue
 *   } catch (HttpException e) {
 *       // handle network issue
 *   }
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportExecutionRestApi {

    private final NetworkClient mNetworkClient;

    ReportExecutionRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    /**
     * Initiates report execution
     *
     * @param executionOptions describes execution configuration metadata
     * @return details of execution invoked on server side
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ReportExecutionDescriptor runReportExecution(@NotNull ReportExecutionRequestOptions executionOptions) throws IOException, HttpException {
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

    /**
     * Provides details of particular execution associated with passed id
     *
     * @param executionId unique identifier used to query details of report execution
     * @return details of requested execution invoked on server side
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ReportExecutionDescriptor requestReportExecutionDetails(@NotNull String executionId) throws IOException, HttpException {
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

    /**
     * Provides status of report execution
     *
     * @param executionId unique identifier used to query status of report execution
     * @return returns one of five states [execution, ready, cancelled, failed, queued]
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ExecutionStatus requestReportExecutionStatus(@NotNull String executionId) throws IOException, HttpException {
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

    /**
     * Cancels existing report execution
     *
     * @param executionId unique identifier used to cancel report execution
     * @return flag that states whether execution cancelled or not
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    public boolean cancelReportExecution(@NotNull String executionId) throws IOException, HttpException {
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

    /**
     * Updates existed report execution with new set of report parameters
     *
     * @param executionId unique identifier used to update report execution
     * @param params the key value pair association that describes parameter id and its values
     * @return flag that states whether operation updated or not
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    public boolean updateReportExecution(@NotNull String executionId,
                                         @NotNull List<ReportParameter> params) throws IOException, HttpException {
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
