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

package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.service.RestClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.InfoCache;
import com.jaspersoft.android.sdk.service.server.ServerInfoService;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InfoCacheManager {
    private final ServerInfoService mInfoService;
    private final InfoCache mInfoCache;
    private final String mServerUrl;

    @TestOnly
    InfoCacheManager(String serverUrl, ServerInfoService infoService, InfoCache infoCache) {
        mServerUrl = serverUrl;
        mInfoService = infoService;
        mInfoCache = infoCache;
    }

    public static InfoCacheManager create(RestClient client) {
        ServerInfoService serverInfoService = ServerInfoService.create(client);
        String baseUrl = client.getServerUrl();
        return new InfoCacheManager(baseUrl, serverInfoService, client.getInfoCache());
    }

    public ServerInfo getInfo() throws ServiceException {
        ServerInfo info = mInfoCache.get(mServerUrl);
        if (info == null) {
            info = mInfoService.requestServerInfo();
            mInfoCache.put(mServerUrl, info);
        }
        return info;
    }

    public void invalidateInfo() {
        mInfoCache.remove(mServerUrl);
    }
}
