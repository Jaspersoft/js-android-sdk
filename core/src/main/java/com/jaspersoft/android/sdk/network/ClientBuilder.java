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

package com.jaspersoft.android.sdk.network;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ClientBuilder {
    private final OkHttpClient mOkHttpClient;
    private RestApiLog mLog = RestApiLog.NONE;
    private long connectTimeout = 10000;
    private long readTimeout = 10000;

    public ClientBuilder() {
        mOkHttpClient = new OkHttpClient();
    }

    public ClientBuilder setLog(RestApiLog logger) {
        mLog = logger;
        return this;
    }

    public ClientBuilder connectionTimeOut(long timeout, TimeUnit unit) {
        connectTimeout = unit.toMillis(timeout);
        return this;
    }

    public ClientBuilder readTimeout(long timeout, TimeUnit unit) {
        readTimeout = unit.toMillis(timeout);
        return this;
    }

    public OkHttpClient build() {
        mOkHttpClient.setReadTimeout(readTimeout, TimeUnit.MILLISECONDS);
        mOkHttpClient.setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        mOkHttpClient.interceptors().add(new LoggingInterceptor(mLog));
        return mOkHttpClient;
    }

    OkHttpClient getClient() {
        return mOkHttpClient;
    }

    void ensureDefaults() {
        if (mLog == null) {
            mLog = RestApiLog.NONE;
        }
    }
}
