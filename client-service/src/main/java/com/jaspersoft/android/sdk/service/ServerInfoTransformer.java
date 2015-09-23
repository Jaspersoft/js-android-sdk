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

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ServerInfoTransformer {

    private static class InstanceHolder {
        private static ServerInfoTransformer INSTANCE = new ServerInfoTransformer();
    }

    private ServerInfoTransformer() {
        // single instance
    }

    public static ServerInfoTransformer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ServerInfo transform(ServerInfoResponse response) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setBuild(response.getBuild());
        serverInfo.setDateFormatPattern(response.getDateFormatPattern());
        serverInfo.setDatetimeFormatPattern(response.getDatetimeFormatPattern());
        serverInfo.setVersion(response.getVersion());
        serverInfo.setEdition(response.getEdition());
        serverInfo.setEditionName(response.getEditionName());
        serverInfo.setFeatures(response.getFeatures());
        return serverInfo;
    }
}