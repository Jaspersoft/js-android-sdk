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

import com.jaspersoft.android.sdk.network.api.auth.Token;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerSession {
    private final TokenProvider mTokenProvider;
    private final InfoProvider mInfoProvider;
    private final String mBaseUrl;

    public ServerSession(Builder builder) {
        mBaseUrl = builder.serverUrl;
        mInfoProvider = builder.infoProvider;
        mTokenProvider = builder.tokenProvider;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public ServerInfo getInfo() {
        return mInfoProvider.provideInfo();
    }

    public Token<?> getTokenProvider() {
        return mTokenProvider.provideToken();
    }

    public static class Builder {
        private String serverUrl;
        private TokenProvider tokenProvider;
        private InfoProvider infoProvider;

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder tokenProvider(TokenProvider tokenProvider) {
            this.tokenProvider = tokenProvider;
            return this;
        }

        public Builder infoProvider(InfoProvider infoProvider) {
            this.infoProvider = infoProvider;
            return this;
        }

        public ServerSession build() {
            return new ServerSession(this);
        }
    }
}
