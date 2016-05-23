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

package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.testkit.dto.Info;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class AnonymousServerTestBundle {
    private final SpringCredentials mCredentials;
    private final AnonymousClient mClient;
    private final Info mInfo;

    public AnonymousServerTestBundle(SpringCredentials credentials, AnonymousClient client, Info info) {
        mCredentials = credentials;
        mClient = client;
        mInfo = info;
    }

    public SpringCredentials getCredentials() {
        return mCredentials;
    }

    public AnonymousClient getClient() {
        return mClient;
    }

    public ServerVersion getVersion() {
        if (mInfo == null) {
            return ServerVersion.valueOf("0");
        }
        return ServerVersion.valueOf(mInfo.getVersion());
    }

    public boolean isPro() {
        if (mInfo == null) {
            return false;
        }
        return "PRO".equals(mInfo.getVersion());
    }

    @Override
    public String toString() {
        return "AnonymousServerTestBundle{" +
                "serverUrl=" + mClient.getBaseUrl() +
                '}';
    }
}
