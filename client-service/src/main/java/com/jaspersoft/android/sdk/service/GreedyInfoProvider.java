/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
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

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

/**
 * Always make call on server
 *
 * @author Tom Koptel
 * @since 2.0
 */
final class GreedyInfoProvider implements InfoProvider {
    private final ServerInfoService mServerInfoService;

    @VisibleForTesting
    GreedyInfoProvider(ServerInfoService serverInfoService) {
        mServerInfoService = serverInfoService;
    }

    public static InfoProvider newInstance(String serverUrl) {
        ServerInfoService service = ServerInfoService.newInstance(serverUrl);
        return new GreedyInfoProvider(service);
    }

    @Override
    @NonNull
    @WorkerThread
    public ServerInfo provideInfo() {
        return mServerInfoService.requestServerInfo()
                .toBlocking().first();
    }
}
