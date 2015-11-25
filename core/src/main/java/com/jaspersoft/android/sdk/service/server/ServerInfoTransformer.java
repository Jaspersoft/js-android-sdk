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

package com.jaspersoft.android.sdk.service.server;

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.service.data.server.ServerEdition;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public static ServerInfoTransformer get() {
        return InstanceHolder.INSTANCE;
    }

    public ServerInfo transform(ServerInfoData response) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setBuild(response.getBuild());

        SimpleDateFormat dateDateFormat = new SimpleDateFormat(response.getDateFormatPattern());
        serverInfo.setDateFormatPattern(dateDateFormat);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(response.getDatetimeFormatPattern());
        serverInfo.setDatetimeFormatPattern(dateTimeFormat);

        double version = VersionParser.INSTANCE.toDouble(response.getVersion());
        serverInfo.setVersion(version);

        ServerEdition edition = ServerEdition.valueOf(response.getEdition());
        serverInfo.setEdition(edition);
        serverInfo.setEditionName(response.getEditionName());

        Set<String> features = parseFeatureSet(response.getFeatures());
        serverInfo.setFeatures(features);

        return serverInfo;
    }

    private Set<String> parseFeatureSet(String features) {
        String[] split = features.split(" ");
        return new HashSet<String>(Arrays.asList(split));
    }
}
