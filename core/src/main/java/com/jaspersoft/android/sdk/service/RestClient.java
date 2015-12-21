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

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.network.Client;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.InMemoryInfoCache;
import com.jaspersoft.android.sdk.service.info.InfoCache;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class RestClient {
    private final Server mServer;
    private final InfoCache mInfoCache;

    private RestClient(Server server, InfoCache infoCache) {
        mServer = server;
        mInfoCache = infoCache;
    }

    public String getServerUrl() {
        return mServer.getBaseUrl();
    }

    public long getReadTimeOut() {
        return mServer.getReadTimeout();
    }

    public long getConnectionTimeOut() {
        return mServer.getConnectTimeout();
    }

    public InfoCache getInfoCache() {
        return mInfoCache;
    }

    public Session authorize(Credentials credentials) throws ServiceException {
        DefaultExceptionMapper exceptionMapper = new DefaultExceptionMapper();
        Client client = mServer.makeAuthorizedClient(credentials).create();
        try {
            client.connect();
        } catch (IOException e) {
            throw exceptionMapper.transform(e);
        } catch (HttpException e) {
            throw exceptionMapper.transform(e);
        }
        return new Session(client, mInfoCache);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Builder() {
        }

        public ConditionalBuilder serverUrl(String serverUrl) {
            return new ConditionalBuilder(serverUrl);
        }
    }

    public static class ConditionalBuilder {
        private final Server.OptionalBuilder mServerBuilder;
        private InfoCache mInfoCache;

        private ConditionalBuilder(String serverUrl) {
            mServerBuilder = Server.newBuilder().withBaseUrl(serverUrl);
        }

        public ConditionalBuilder withReadTimeout(int timeOut, TimeUnit unit) {
            mServerBuilder.withReadTimeout(timeOut, unit);
            return this;
        }

        public ConditionalBuilder withConnectionTimeOut(int timeOut, TimeUnit unit) {
            mServerBuilder.withConnectionTimeOut(timeOut, unit);
            return this;
        }

        public ConditionalBuilder withInfoCache(InfoCache infoCache) {
            mInfoCache = infoCache;
            return this;
        }

        public RestClient create() {
            if (mInfoCache == null) {
                mInfoCache = new InMemoryInfoCache();
            }
            Server server = mServerBuilder.create();
            return new RestClient(server, mInfoCache);
        }
    }
}
