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

import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.service.info.InMemoryInfoCache;
import com.jaspersoft.android.sdk.service.info.InfoCache;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class RestClient {
    private final String mServerUrl;
    private final long mReadTimeOut;
    private final long mConnectionTimeOut;
    private final long mPollTimeout;
    private final InfoCache mInfoCache;


    private RestClient(String serverUrl, long readTimeOut, long connectionTimeOut, long pollTimeout, InfoCache infoCache) {
        mServerUrl = serverUrl;
        mReadTimeOut = readTimeOut;
        mConnectionTimeOut = connectionTimeOut;
        mPollTimeout = pollTimeout;
        mInfoCache = infoCache;
    }


    public String getServerUrl() {
        return mServerUrl;
    }

    public long getReadTimeOut() {
        return mReadTimeOut;
    }

    public long getConnectionTimeOut() {
        return mConnectionTimeOut;
    }

    public long getPollTimeout() {
        return mPollTimeout;
    }

    public InfoCache getInfoCache() {
        return mInfoCache;
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
        private final String mServerUrl;
        private long mConnectionReadTimeOut = TimeUnit.SECONDS.toMillis(10);
        private long mConnectionTimeOut = TimeUnit.SECONDS.toMillis(10);
        private long mPollTimeOut = TimeUnit.SECONDS.toMillis(1);
        private InfoCache mInfoCache;

        private ConditionalBuilder(String serverUrl) {
            mServerUrl = serverUrl;
        }

        public ConditionalBuilder pollTimeOut(int timeOut, TimeUnit unit) {
            mPollTimeOut = unit.toMillis(timeOut);
            return this;
        }

        public ConditionalBuilder readTimeOut(int timeOut, TimeUnit unit) {
            mConnectionReadTimeOut = unit.toMillis(timeOut);
            return this;
        }

        public ConditionalBuilder connectionTimeOut(int timeOut, TimeUnit unit) {
            mConnectionTimeOut = unit.toMillis(timeOut);
            return this;
        }

        public ConditionalBuilder infoCache(InfoCache infoCache) {
            mInfoCache = infoCache;
            return this;
        }

        public RestClient create() {
            if (mInfoCache == null) {
                mInfoCache = new InMemoryInfoCache();
            }
            return new RestClient(mServerUrl, mConnectionReadTimeOut, mConnectionTimeOut, mPollTimeOut, mInfoCache);
        }
    }
}
