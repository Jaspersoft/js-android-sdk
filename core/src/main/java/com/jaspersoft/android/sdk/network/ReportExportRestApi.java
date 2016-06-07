/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

import java.io.IOException;

/**
 * Public API that allows initiating report export request, export execution status, request export and its attachment content
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
 *
 *   ReportExportRestApi reportExportRestApi = client.reportExportApi();
 *
 *   String reportExecutionId = "ef2-rtg-456";
 *   try {
 *       ExecutionRequestOptions options = ExecutionRequestOptions.create();
 *       ExportExecutionDescriptor exportDetails = reportExportRestApi.runExportExecution(reportExecutionId, options);
 *       String exportId = exportDetails.getExportId();
 *
 *       ExecutionStatus status = reportExportRestApi.checkExportExecutionStatus(reportExecutionId, exportId);
 *
 *
 *       ExportOutputResource exportOutput = reportExportRestApi.requestExportOutput(reportExecutionId, exportId);
 *       // Consume stream in file descriptor
 *       InputStream exportRawContent = exportOutput.getOutputResource().getStream();
 *
 *
 *       // To gain attachment ID one should recall details of whole report execution
 *       ReportExecutionDescriptor executionDetails = client.reportExecutionApi().requestReportExecutionDetails(reportExecutionId);
 *       Set<ExportDescriptor> exports = executionDetails.getExports();
 *       for (ExportDescriptor export : exports) {
 *           if (export.getId().equals(exportId)) {
 *               Set<AttachmentDescriptor> attachments = export.getAttachments();
 *               for (AttachmentDescriptor attachment : attachments) {
 *
 *                   String fileName = attachment.getFileName();
 *                   OutputResource resource = reportExportRestApi.requestExportAttachment(reportExecutionId, exportId, fileName);
 *                   InputStream attachmentRawContent = resource.getStream();
 *
 *               }
 *           }
 *       }
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
public class ReportExportRestApi {
    private final NetworkClient mNetworkClient;

    ReportExportRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    /**
     * Initiates export execution for specified report
     *
     * @param executionId unique identifier used to associate current export with execution
     * @param executionOptions describes execution configuration metadata
     * @return details of execution invoked on server side
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ExportExecutionDescriptor runExportExecution(@NotNull String executionId,
                                                        @NotNull ExecutionRequestOptions executionOptions) throws IOException, HttpException {
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

    /**
     * Provides status of export execution
     *
     * @param executionId unique identifier used to identify report execution
     * @param exportId unique identifier used to query status of export execution
     * @return returns one of five states [execution, ready, cancelled, failed, queued]
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ExecutionStatus checkExportExecutionStatus(@NotNull String executionId,
                                                      @NotNull String exportId) throws IOException, HttpException {
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

    /**
     * Provides export metadata and content as raw stream of bytes
     *
     * @param executionId unique identifier used to identify report execution
     * @param exportId unique identifier used to query export content
     * @return resources that encapsulates metadata and raw data of export
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ExportOutputResource requestExportOutput(@NotNull String executionId,
                                                    @NotNull String exportId) throws IOException, HttpException {
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

    /**
     * Provides attachment content as raw stream of bytes
     *
     * @param executionId unique identifier used to identify report execution
     * @param exportId unique identifier used to identify export execution
     * @param attachmentId filename of attachment
     * @return raw stream of attachment
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public OutputResource requestExportAttachment(@NotNull String executionId,
                                                  @NotNull String exportId,
                                                  @NotNull String attachmentId) throws IOException, HttpException {
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