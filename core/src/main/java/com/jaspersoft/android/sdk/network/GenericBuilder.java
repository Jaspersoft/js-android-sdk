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

import retrofit.Retrofit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class GenericBuilder<TargetBuilder, Api> {
    protected final ClientBuilder clientBuilder;
    protected final AdapterBuilder adapterBuilder;

    public GenericBuilder() {
        clientBuilder = new ClientBuilder();
        adapterBuilder = new AdapterBuilder(clientBuilder);
    }

    @SuppressWarnings("unchecked")
    public TargetBuilder baseUrl(String baseUrl) {
        adapterBuilder.baseUrl(baseUrl);
        return (TargetBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public TargetBuilder logger(RestApiLog log) {
        clientBuilder.setLog(log);
        return (TargetBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public TargetBuilder connectionTimeOut(long timeout) {
        clientBuilder.connectionTimeOut(timeout);
        return (TargetBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public TargetBuilder readTimeout(long timeout) {
        clientBuilder.readTimeout(timeout);
        return (TargetBuilder) this;
    }

    void ensureDefaults() {
        clientBuilder.ensureDefaults();
        adapterBuilder.ensureDefaults();
    }

    Retrofit.Builder getAdapter() {
        return adapterBuilder.getAdapter();
    }

    abstract Api createApi();

    public Api build() {
        ensureDefaults();
        return createApi();
    }
}
