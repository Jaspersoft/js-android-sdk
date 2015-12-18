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

package com.jaspersoft.android.sdk.network;

import com.squareup.okhttp.OkHttpClient;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Server {

    private final String mBaseUrl;

    public Server(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public ClientFactory<AnonymousClient> newClient() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setFollowRedirects(false);
        return new ClientFactory<AnonymousClient>(mBaseUrl, okHttpClient) {
            @Override
            public AnonymousClient create() {
                return new AnonymousClientImpl(mBaseUrl, okHttpClient);
            }
        };
    }

    public ClientFactory<AuthorizedClient> newClient(final Credentials credentials) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        return new ClientFactory<AuthorizedClient>(mBaseUrl, okHttpClient) {
            @Override
            public AuthorizedClient create() {
                return new AuthorizedClientImpl(mBaseUrl, mOkHttpClient, credentials);
            }
        };
    }

    public static Server create(String baseUrl) {
        return new Server(baseUrl);
    }
}
