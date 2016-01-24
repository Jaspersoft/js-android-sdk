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

import com.google.gson.JsonSyntaxException;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
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
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportOptionRestApi {
    private final NetworkClient mNetworkClient;

    ReportOptionRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    @NotNull
    public Set<ReportOption> requestReportOptionsList(@Nullable String reportUnitUri) throws IOException, HttpException {
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
            return reportOptionSet.get();
        } catch (JsonSyntaxException ex) {
            /**
             * This possible when there is no report options
             * API responds with plain/text message: 'No options found for {URI}'
             * As soon as there 2 options to reserve this we decide to swallow exception and return empty object
             */
            return Collections.emptySet();
        }
    }

    @NotNull
    public ReportOption createReportOption(
            @Nullable String reportUnitUri,
            @Nullable String optionLabel,
            @Nullable List<ReportParameter> parameters,
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
        return mNetworkClient.deserializeJson(response, ReportOption.class);
    }

    public void updateReportOption(@Nullable String reportUnitUri,
                                   @Nullable String optionId,
                                   @Nullable List<ReportParameter> parameters) throws IOException, HttpException {
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

    public void deleteReportOption(@Nullable String reportUnitUri,
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
