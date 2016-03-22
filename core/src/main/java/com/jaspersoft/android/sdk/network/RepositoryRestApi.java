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
 * Public API that allows searching within JRS repository, request resource details and content output, list dashboard components
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
 *    RepositoryRestApi repositoryRestApi = client.repositoryApi();
 *
 *    String reportUri = "/my/report/uri";
 *    String dashboardUri = "/my/dashboard/uri";
 *    String reportType = "reportUnit";
 *
 *    try {
 *        Map<String, Object> params = new HashMap<>();
 *        params.put("q", "all reports");
 *        ResourceSearchResult searchResult = repositoryRestApi.searchResources(params);
 *
 *
 *        ResourceLookup lookup = repositoryRestApi.requestResource(reportUri, reportType);
 *
 *        OutputResource resource = repositoryRestApi.requestResourceOutput(reportUri);
 *        InputStream resourceRawContent = resource.getStream();
 *
 *        DashboardComponentCollection components = repositoryRestApi.requestDashboardComponents(dashboardUri);
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
public class RepositoryRestApi {
    private final NetworkClient mNetworkClient;

    RepositoryRestApi(NetworkClient networkClient) {
        mNetworkClient = networkClient;
    }

    /**
     * Performs search requests
     *
     * @param searchParams one used to specify concrete requests
     * @return search result collection that encapsulates offset, limit, total count metadata
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
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

    /**
     * Provides additional metadata for specific resource of specified type
     *
     * @param resourceUri uri we are requesting metadata for
     * @param type of resource can be reportUnit, file, folder
     * @return resource lookup subclass. See {@link FileLookup}, {@link FolderLookup}, {@link ReportLookup}
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ResourceLookup requestResource(@NotNull String resourceUri, @NotNull String type) throws IOException, HttpException {
        Utils.checkNotNull(resourceUri, "Report uri should not be null");
        Utils.checkNotNull(type, "Report type should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(resourceUri)
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

    /**
     * Provides collection of dashboard components
     *
     * @param dashboardUri uri associated with particular dashboard
     * @return collection of of components. One encapsulate data related to the dashboard input controls
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public DashboardComponentCollection requestDashboardComponents(@NotNull String dashboardUri) throws IOException, HttpException {
        Utils.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        HttpUrl url = new PathResolver.Builder()
                .addPath("rest_v2")
                .addPath("resources")
                .addPaths(dashboardUri + "_files")
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

    /**
     * Provides raw data of stream for specific resource
     *
     * @param resourceUri uri of resource we are requesting content of
     * @return resource output
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
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
