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

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerRestApi {

    private final NetworkClient mClientWrapper;

    public ServerRestApi(NetworkClient networkClient) {
        mClientWrapper = networkClient;
    }

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

    @NotNull
    public String requestBuild() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/build");
    }

    @NotNull
    public String requestEdition() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/edition");
    }

    @NotNull
    public String requestVersion() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/version");
    }

    @NotNull
    public String requestFeatures() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/features");
    }

    @NotNull
    public String requestEditionName() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/editionName");
    }

    @NotNull
    public String requestLicenseType() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/licenseType");
    }

    @NotNull
    public String requestExpiration() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/expiration");
    }

    @NotNull
    public String requestDateFormatPattern() throws IOException, HttpException {
        return plainRequest("rest_v2/serverInfo/dateFormatPattern");
    }

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
