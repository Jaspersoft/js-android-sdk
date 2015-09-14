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

package com.jaspersoft.android.sdk.network.api;

import com.jaspersoft.android.sdk.network.api.auth.Token;

import retrofit.Retrofit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class GenericAuthBuilder<TargetBuilder, Api> {
    private final ClientBuilder mClientBuilder;
    private final AdapterBuilder mAdapterBuilder;
    private final AuthBuilder mAuthBuilder;

    public GenericAuthBuilder() {
        mClientBuilder = new ClientBuilder();
        mAdapterBuilder = new AdapterBuilder(mClientBuilder);
        mAuthBuilder = new AuthBuilder(mAdapterBuilder);
    }

    @SuppressWarnings("unchecked")
    public TargetBuilder baseUrl(String baseUrl) {
        mAdapterBuilder.baseUrl(baseUrl);
        return  (TargetBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public TargetBuilder log(RestApiLog log) {
        mClientBuilder.setLog(log);
        return (TargetBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public TargetBuilder token(Token<?> token) {
        mAuthBuilder.setToken(token);
        return (TargetBuilder) this;
    }

    abstract Api createApi();

    public Api build() {
        ensureDefaults();
        return createApi();
    }

    void ensureDefaults() {
        mClientBuilder.ensureDefaults();
        mAdapterBuilder.ensureDefaults();
        mAuthBuilder.ensureDefaults();
    }

    Retrofit createAdapter() {
        return mAuthBuilder.createAdapter();
    }
}
