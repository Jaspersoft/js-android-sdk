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

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.TestCookieManager;

import java.io.IOException;
import java.net.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class LazyClient {
    private final JrsMetadata mJrsMetadata;
    private AuthorizedClient mAuthorizedClient;
    private AnonymousClient mAnonymousClient;
    public static final InetSocketAddress CHARLES_ADDRESS = new InetSocketAddress("0.0.0.0", 8888);

    public LazyClient(JrsMetadata jrsMetadata) {
        mJrsMetadata = jrsMetadata;
    }

    public static Server getServer(String serverUrl) {
        Server.OptionalBuilder serverBuilder = Server.builder()
                .withBaseUrl(serverUrl);
        if (isProxyReachable()) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, CHARLES_ADDRESS);
            serverBuilder.withProxy(proxy);
        }
        return serverBuilder.build();
    }

    private static boolean isProxyReachable() {
        try {
            Socket socket = new Socket();
            socket.connect(CHARLES_ADDRESS);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public AnonymousClient getAnonymousClient() {
        if (mAnonymousClient == null) {
            mAnonymousClient = getServer(mJrsMetadata.getServerUrl())
                    .newClient()
                    .withCookieHandler(new TestCookieManager())
                    .create();
        }
        return mAnonymousClient;
    }

    public AuthorizedClient getAuthorizedClient() {
        if (mAuthorizedClient == null) {
            mAuthorizedClient = getServer(mJrsMetadata.getServerUrl())
                    .newClient(mJrsMetadata.getCredentials())
                    .withCookieHandler(new TestCookieManager())
                    .create();
        }
        return mAuthorizedClient;
    }
}
