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

import com.jaspersoft.android.sdk.network.entity.resource.FolderLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public ResourceSearchResult searchResources(@Nullable Map<String, Object> searchParams) throws IOException, HttpException {
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
    public ReportLookup requestReportResource(@Nullable String resourceUri) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Report uri should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(resourceUri)
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/repository.reportUnit+json")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response rawResponse = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(rawResponse, ReportLookup.class);
    }

    @NotNull
    public FolderLookup requestFolderResource(@Nullable String resourceUri) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Folder uri should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(resourceUri)
                .build()
                .resolve(mNetworkClient.getBaseUrl());

        Request request = new Request.Builder()
                .addHeader("Accept", "application/repository.folder+json")
                .get()
                .url(url)
                .build();

        com.squareup.okhttp.Response rawResponse = mNetworkClient.makeCall(request);
        return mNetworkClient.deserializeJson(rawResponse, FolderLookup.class);
    }
}
