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

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Public API allows requesting server info public information
 *
 * <pre>
 * {@code
 *
 *    Server server = Server.builder()
 *            .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *            .build();
 *
 *    AnonymousClient client = server.newClient().create();
 *    ServerRestApi restApi = client.infoApi();
 *
 *    try {
 *        ServerInfoData serverInfoData = restApi.requestServerInfo();
 *
 *        String edition = restApi.requestEdition();
 *        String editionName = restApi.requestEditionName();
 *        String version = restApi.requestVersion();
 *        String build = restApi.requestBuild();
 *        String licenseType = restApi.requestLicenseType();
 *        String expiration = restApi.requestExpiration();
 *        String features = restApi.requestFeatures();
 *        String dateFormatPattern = restApi.requestDateFormatPattern();
 *        String dateTimeFormatPattern = restApi.requestDateTimeFormatPattern();
 *    } catch (IOException e) {
 *        // handle socket issue
 *    } catch (HttpException e) {
 *        // handle network issue
 *    }
 * }
 * </pre>
 * @author Tom Koptel
 * @since 2.3
 */
public class ServerRestApi {

    private final NetworkClient mClientWrapper;

    public ServerRestApi(NetworkClient networkClient) {
        mClientWrapper = networkClient;
    }

    /**
     * Provides server info metadata as whole DTO object
     *
     * @return server info publicly available metadata
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public ServerInfoData requestServerInfo() throws IOException, HttpException {
        HttpUrl httpUrl = mClientWrapper.getBaseUrl().resolve("rest_v2/serverInfo");
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json; charset=UTF-8")
                .url(httpUrl)
                .get()
                .build();
        Response response = mClientWrapper.makeCall(request);
        return mClientWrapper.deserializeJson(response, ServerInfoData.class);
    }

    /**
     * Provides build number of JRS
     *
     * @return build number
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestBuild() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/build");
    }

    /**
     * Provides edition of JRS
     *
     * @return PRO or CE
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestEdition() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/edition");
    }

    /**
     * Provides version of JRS
     *
     * @return version of current JRS
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestVersion() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/version");
    }

    /**
     * Provides comma separated feature list
     *
     * @return list of features
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestFeatures() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/features");
    }

    /**
     * Provides edition name that corresponds to type of JRS
     *
     * @return edition name. E.g. Enterprise
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestEditionName() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/editionName");
    }

    /**
     * Provides license type of particular JRS instance
     *
     * @return license type
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestLicenseType() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/licenseType");
    }

    /**
     * Provides expiration date of license
     *
     * @return expiration date
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestExpiration() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/expiration");
    }

    /**
     * Provides JRS date format
     *
     * @return date format
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestDateFormatPattern() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/dateFormatPattern");
    }

    /**
     * Provides JRS data time format
     *
     * @return date time format
     * @throws IOException   if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    @NotNull
    public String requestDateTimeFormatPattern() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/datetimeFormatPattern");
    }

    private String plainRequest(String path) throws IOException, HttpException {
        HttpUrl httpUrl = mClientWrapper.getBaseUrl().resolve(path);
        Request request = new Request.Builder()
                .addHeader("Accept", "text/plain; charset=UTF-8")
                .url(httpUrl)
                .get()
                .build();
        Response response = mClientWrapper.makeCall(request);
        return mClientWrapper.deserializeString(response);
    }
}
