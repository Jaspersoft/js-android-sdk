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

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.network.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

import org.jetbrains.annotations.TestOnly;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerInfoService {
    private final ServerRestApi mRestApi;
    private final ServerInfoTransformer mTransformer;

    @TestOnly
    ServerInfoService(ServerRestApi restApi, ServerInfoTransformer transformer) {
        mRestApi = restApi;
        mTransformer = transformer;
    }

    public static ServerInfoService newInstance(ServerRestApi restApi) {
        return new ServerInfoService(restApi, ServerInfoTransformer.getInstance());
    }

    public static ServerInfoService newInstance(String baseUrl) {
        ServerRestApi restApi = new ServerRestApi.Builder()
                .baseUrl(baseUrl)
                .build();

        return new ServerInfoService(restApi, ServerInfoTransformer.getInstance());
    }

    public ServerInfo requestServerInfo() {
        ServerInfoData response = mRestApi.requestServerInfo();
        return mTransformer.transform(response);
    }

    public ServerVersion requestServerVersion() {
        String version = mRestApi.requestVersion();
        return ServerVersion.defaultParser().parse(version);
    }

    public SimpleDateFormat requestServerDateTimeFormat() {
        String dateTimeFormat = mRestApi.requestDateTimeFormatPattern();
        return new SimpleDateFormat(dateTimeFormat);
    }
}
