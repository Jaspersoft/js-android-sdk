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

import com.google.gson.JsonSyntaxException;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionSet;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Public API that allows create/read/update/delete report options. Report option stands for filters user previously saved
 *
 * <pre>
 * {@code
 *
 *    Server server = Server.builder()
 *            .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *            .build();
 *
 *    Credentials credentials = SpringCredentials.builder()
 *            .withPassword("phoneuser")
 *            .withUsername("phoneuser")
 *            .withOrganization("organization_1")
 *            .build();
 *
 *
 *    AuthorizedClient client = server.newClient(credentials)
 *            .create();
 *
 *    ReportOptionRestApi reportOptionRestApi = client.reportOptionsApi();
 *
 *    String reportUri = "/my/uri";
 *
 *    try {
 *        Set<ReportOptionEntity> options = reportOptionRestApi.requestReportOptionsList(reportUri);
 *        List<ReportOptionEntity> optionsList = new ArrayList<>(options);
 *        ReportOptionEntity option = optionsList.get(0);
 *        String optionId = option.getId();
 *
 *        List<ReportParameter> parameters = Collections.singletonList(
 *                new ReportParameter("key", Collections.singleton("value"))
 *        );
 *        reportOptionRestApi.updateReportOption(reportUri, optionId, parameters);
 *        reportOptionRestApi.deleteReportOption(reportUri, optionId);
 *
 *
 *        boolean ovewrite = true;
 *        String label = "my label";
 *        ReportOptionEntity reportOption = reportOptionRestApi.createReportOption(reportUri, label, parameters, true);
 *    } catch (IOException e) {
 *        // handle socket issue
 *    } catch (HttpException e) {
 *        // handle network issue
 *    }
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportOptionRestApi {
    private final NetworkClient mNetworkClient;

    ReportOptionRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    /**
     * Provides list of report options
     *
     * @param reportUnitUri uri to query options for
     * @return list of report options DTO
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public Set<ReportOptionEntity> requestReportOptionsList(
            @NotNull String reportUnitUri) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reports")
                .addPaths(reportUnitUri)
                .addPath("options")
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        Response response = mNetworkClient.makeCall(request);
        try {
            ReportOptionSet reportOptionSet = mNetworkClient.deserializeJson(response, ReportOptionSet.class);
            return Collections.unmodifiableSet(reportOptionSet.get());
        } catch (JsonSyntaxException ex) {
            /**
             * This possible when there is no report options
             * API responds with plain/text message: 'No options found for {URI}'
             * As soon as there 2 options to reserve this we decide to swallow exception and return empty object
             */
            return Collections.emptySet();
        }
    }

    /**
     * Creates report option
     *
     * @param reportUnitUri uri of report we are creating report option for
     * @param optionLabel   label of report option
     * @param parameters    key/value pairs that represent parameters
     * @param overwrite     tells whether to overwrite report option if the one with same label exists
     * @return report option DTO
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ReportOptionEntity createReportOption(
            @NotNull String reportUnitUri,
            @NotNull String optionLabel,
            @NotNull List<ReportParameter> parameters,
            boolean overwrite) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");
        Utils.checkNotNull(optionLabel, "Option label should not be null");
        Utils.checkNotNull(parameters, "Parameters values should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reports")
                .addPaths(reportUnitUri)
                .addPath("options")
                .build()
                .resolve(mNetworkClient.getBaseUrl())
                .newBuilder()
                .addQueryParameter("label", optionLabel)
                .addQueryParameter("overwrite", String.valueOf(overwrite))
                .build();

        Map<String, Set<String>> controlsValues = ReportParamsMapper.INSTANCE.toMap(parameters);
        RequestBody requestBody = mNetworkClient.createJsonRequestBody(controlsValues);

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .post(requestBody)
                .url(url)
                .build();

        Response response = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(response, ReportOptionEntity.class);
    }

    /**
     * Updates report options data
     *
     * @param reportUnitUri uri of report we are updating report option for
     * @param optionId      unique identifier that is associated with particular report option
     * @param parameters    key/value pairs that represent parameters
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    public void updateReportOption(
            @NotNull String reportUnitUri,
            @NotNull String optionId,
            @NotNull List<ReportParameter> parameters) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");
        Utils.checkNotNull(optionId, "Option id should not be null");
        Utils.checkNotNull(parameters, "Parameters values should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reports")
                .addPaths(reportUnitUri)
                .addPath("options")
                .addPath(optionId)
                .build()
                .resolve(mNetworkClient.getBaseUrl());


        Map<String, Set<String>> controlsValues = ReportParamsMapper.INSTANCE.toMap(parameters);
        RequestBody requestBody = mNetworkClient.createJsonRequestBody(controlsValues);

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .put(requestBody)
                .url(url)
                .build();

        mNetworkClient.makeCall(request);
    }

    /**
     * Deletes report option
     *
     * @param reportUnitUri uri of report we are deleting report option for
     * @param optionId      unique identifier that is associated with particular report option
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    public void deleteReportOption(
            @Nullable String reportUnitUri,
            @Nullable String optionId) throws IOException, HttpException {
        Utils.checkNotNull(reportUnitUri, "Report uri should not be null");
        Utils.checkNotNull(optionId, "Option id should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("reports")
                .addPaths(reportUnitUri)
                .addPath("options")
                .addPath(optionId)
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .delete()
                .url(url)
                .build();

        mNetworkClient.makeCall(request);
    }
}
