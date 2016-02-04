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

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.network.entity.resource.*;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryRestApi {
    private final NetworkClient mNetworkClient;

    RepositoryRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    @NotNull
    public ResourceSearchResult searchResources(@NotNull Map<String, Object> searchParams) throws IOException, HttpException {
        HttpUrl url = mNetworkClient.getBaseUrl()
                .newBuilder()
                .addPathSegment("rest_v2")
                .addPathSegment("resources")
                .build();
        url = QueryMapper.INSTANCE.mapParams(searchParams, url);

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response rawResponse = mNetworkClient.makeCall(request);

        int status = rawResponse.code();
        ResourceSearchResult entity;
        if (status == 204) {
            entity = ResourceSearchResult.empty();
        } else {
            entity = mNetworkClient.deserializeJson(rawResponse, ResourceSearchResult.class);
            com.squareup.okhttp.Headers headers = rawResponse.headers();

            int resultCount = Utils.headerToInt(headers, "Result-Count");
            int totalCount = Utils.headerToInt(headers, "Total-Count");
            int startIndex = Utils.headerToInt(headers, "Start-Index");
            int nextOffset = Utils.headerToInt(headers, "Next-Offset");

            entity.setResultCount(resultCount);
            entity.setTotalCount(totalCount);
            entity.setStartIndex(startIndex);
            entity.setNextOffset(nextOffset);
        }
        return entity;
    }

    @NotNull
    public ResourceLookup requestResource(@NotNull String resourceUri, boolean expanded) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Resource uri should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(resourceUri)
                .build()
                .resolve(mNetworkClient.getBaseUrl())
                .newBuilder()
                .addQueryParameter("expanded", String.valueOf(expanded))
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response rawResponse = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(rawResponse, ResourceLookup.class);
    }

    @NotNull
    public ResourceLookup requestResource(@NotNull String reportUri, @NotNull String type) throws IOException, HttpException {
        Utils.checkNotNull(reportUri, "Report uri should not be null");
        Utils.checkNotNull(type, "Report type should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(reportUri)
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", String.format("application/repository.%s+json", type))
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response rawResponse = mNetworkClient.makeCall(request);
        if ("file".equals(type)) {
            return mNetworkClient.deserializeJson(rawResponse, FileLookup.class);
        }
        if ("folder".equals(type)) {
            return mNetworkClient.deserializeJson(rawResponse, FolderLookup.class);
        }
        if ("reportUnit".equals(type)) {
            return mNetworkClient.deserializeJson(rawResponse, ReportLookup.class);
        }
        return mNetworkClient.deserializeJson(rawResponse, ResourceLookup.class);
    }

    @NotNull
    public DashboardComponentCollection requestDashboardComponents(@NotNull String resourceUri) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Dashboard uri should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(resourceUri + "_files")
                .addPath("components")
                .build()
                .resolve(mNetworkClient.getBaseUrl())
                .newBuilder()
                .addQueryParameter("expanded", "false")
                .build();

        Request request = new Request.Builder()
                .addHeader("Accept", "application/dashboardComponentsSchema+json")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response rawResponse = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(rawResponse, DashboardComponentCollection.class);
    }

    @NotNull
    public OutputResource requestResourceOutput(@NotNull String resourceUri) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Resource uri should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(resourceUri)
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response rawResponse = mNetworkClient.makeCall(request);
        return new RetrofitOutputResource(rawResponse.body());
    }
}
