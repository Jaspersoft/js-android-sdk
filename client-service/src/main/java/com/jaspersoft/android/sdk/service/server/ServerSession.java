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

package com.jaspersoft.android.sdk.service.server;

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.api.auth.Token;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerSession {
    private final TokenProvider mTokenProvider;
    private final InfoProvider mInfoProvider;
    private final String mServerUrl;

    ServerSession(@NonNull String serverUrl,
                  @NonNull TokenProvider tokenProvider,
                  @NonNull InfoProvider infoProvider) {
        mTokenProvider = tokenProvider;
        mInfoProvider = infoProvider;
        mServerUrl = serverUrl;
    }

    @NonNull
    public String getServerUrl() {
        return mServerUrl;
    }

    @NonNull
    public ServerInfo getInfo() {
        return mInfoProvider.provideInfo();
    }

    @NonNull
    public Token<?> getToken() {
        return mTokenProvider.provideToken();
    }

    public static class Builder {
        private String serverUrl;
        private TokenProvider tokenProvider;
        private InfoProvider infoProvider;

        public Builder serverUrl(@NonNull String serverUrl) {
            Utils.checkNotNull(serverUrl, "serverUrl == null");
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder tokenProvider(@NonNull TokenProvider tokenProvider) {
            Utils.checkNotNull(serverUrl, "tokenProvider == null");
            this.tokenProvider = tokenProvider;
            return this;
        }

        @NonNull
        public ServerSession build() {
            ensureState();
            ensureSaneDefaults();

            return new ServerSession(serverUrl, tokenProvider, infoProvider);
        }

        private void ensureState() {
            if (serverUrl == null) {
                throw new IllegalStateException("Session requires server url");
            }
            if (tokenProvider == null) {
                throw new IllegalStateException("Session requires token provider");
            }
        }

        private void ensureSaneDefaults() {
            infoProvider = GreedyInfoProvider.newInstance(serverUrl);
        }
    }
}
