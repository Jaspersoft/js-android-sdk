/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.text.SimpleDateFormat;

/**
 * Always make call on server
 *
 * @author Tom Koptel
 * @since 2.0
 */
final class GreedyInfoProvider implements InfoProvider {
    private final ServerInfoService mServerInfoService;
    private final String mBaseUrl;

    @TestOnly
    GreedyInfoProvider(ServerInfoService serverInfoService, String serverUrl) {
        mServerInfoService = serverInfoService;
        mBaseUrl = serverUrl;
    }

    public static InfoProvider newInstance(String serverUrl) {
        ServerInfoService service = ServerInfoService.newInstance(serverUrl);
        return new GreedyInfoProvider(service, serverUrl);
    }

    @NotNull
    @Override
    public String getBaseUrl() {
        return mBaseUrl;
    }

    @Override
    @NotNull
    public ServerInfo provideInfo() {
        return mServerInfoService.requestServerInfo();
    }

    @NotNull
    @Override
    public ServerVersion provideVersion() {
        return mServerInfoService.requestServerVersion();
    }

    @NotNull
    @Override
    public SimpleDateFormat provideDateTimeFormat() {
        return mServerInfoService.requestServerDateTimeFormat();
    }
}
