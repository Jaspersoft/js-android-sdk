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

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.TestCookieManager;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class LazyClient {
    private final JrsMetadata mJrsMetadata;
    private AuthorizedClient mClient;

    public LazyClient(JrsMetadata jrsMetadata) {
        mJrsMetadata = jrsMetadata;
    }

    public static Server getServer(String serverUrl) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("0.0.0.0", 8888));
        return Server.newBuilder()
                .withBaseUrl(serverUrl)
                .withProxy(proxy)
                .build();
    }

    public AuthorizedClient getAuthorizedClient() {
        if (mClient == null) {
            mClient = getServer(mJrsMetadata.getServerUrl())
                    .newClient(mJrsMetadata.getCredentials())
                    .withCookieHandler(new TestCookieManager())
                    .create();
        }
        return mClient;
    }
}
