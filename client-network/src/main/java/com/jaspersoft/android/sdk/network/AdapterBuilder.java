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

package com.jaspersoft.android.sdk.network;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Converter;
import retrofit.Retrofit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class AdapterBuilder {
    private final Retrofit.Builder mRestAdapterBuilder;
    private final Converter.Factory mGsonConverterFactory;
    private final Converter.Factory mStringConverterFactory;

    final ClientBuilder clientBuilder;
    HttpUrl baseUrl;

    public AdapterBuilder(ClientBuilder clientBuilder){
        mRestAdapterBuilder = new Retrofit.Builder();

        Gson configuredGson = GsonFactory.create();
        mGsonConverterFactory = GsonConverterFactory.create(configuredGson);
        mStringConverterFactory = StringConverterFactory.create();

        this.clientBuilder = clientBuilder;
    }

    @SuppressWarnings("unchecked")
    public AdapterBuilder baseUrl(String baseUrl) {
        Utils.checkNotNull(baseUrl, "baseUrl == null");
        baseUrl = Utils.normalizeBaseUrl(baseUrl);
        HttpUrl httpUrl = HttpUrl.parse(baseUrl);
        if (httpUrl == null) {
            throw new IllegalArgumentException("Illegal URL: " + baseUrl);
        }
        this.baseUrl = httpUrl;
        return this;
    }

    public void ensureDefaults() {
        if (baseUrl == null) {
            throw new IllegalStateException("Base url should be supplied to work with JRS API");
        }
    }

    Retrofit.Builder getAdapter() {
        OkHttpClient client = clientBuilder.build();
        mRestAdapterBuilder.client(client);
        mRestAdapterBuilder.baseUrl(baseUrl);

        mRestAdapterBuilder.addConverterFactory(mStringConverterFactory);
        mRestAdapterBuilder.addConverterFactory(mGsonConverterFactory);

        return mRestAdapterBuilder;
    }
}
