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

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ResourceTestBundle {
    private final String mUri;
    private final ServerTestBundle serverTestBundle;

    public ResourceTestBundle(String mUri, ServerTestBundle serverTestBundle) {
        this.mUri = mUri;
        this.serverTestBundle = serverTestBundle;
    }

    public String getUri() {
        return mUri;
    }

    public ServerVersion getVersion() {
        return serverTestBundle.getVersion();
    }

    public SpringCredentials getCredentials() {
        return serverTestBundle.getCredentials();
    }

    public AuthorizedClient getClient() {
        return serverTestBundle.getClient();
    }

    @Override
    public String toString() {
        return "ReportTestBundle{" +
                "server=" + serverTestBundle +
                ", uri='" + mUri + '\'' +
                '}';
    }
}
