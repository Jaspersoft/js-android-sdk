/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.server.ServerInfoService;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class InfoCacheManagerImpl implements InfoCacheManager {
    private final ServerInfoService mInfoService;
    private final InfoCache mInfoCache;

    @TestOnly
    InfoCacheManagerImpl(ServerInfoService infoService, InfoCache infoCache) {
        mInfoService = infoService;
        mInfoCache = infoCache;
    }

    public static InfoCacheManager create(RestClient client, InfoCache infoCache) {
        ServerInfoService serverInfoService = ServerInfoService.create(client);
        return new InfoCacheManagerImpl(serverInfoService, infoCache);
    }

    @Override
    public ServerInfo getInfo() throws ServiceException {
        ServerInfo info = mInfoCache.get();
        if (info == null) {
            info = mInfoService.requestServerInfo();
            mInfoCache.put(info);
        }
        return info;
    }

    @Override
    public void invalidateInfo() {
        mInfoCache.evict();
    }
}
