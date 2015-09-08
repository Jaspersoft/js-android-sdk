/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * TODO separate OkHttp client creation from Retrofit client
 *
 * @author Tom Koptel
 * @since 2.0
 */
abstract class BaseBuilder<API, SubBuilder> {
    private final Retrofit.Builder mRestAdapterBuilder;
    private final OkHttpClient mOkHttpClient;

    private RestApiLog mLog = RestApiLog.NONE;

    public BaseBuilder(String baseUrl){
        if (baseUrl == null || baseUrl.length() == 0) {
            throw new IllegalArgumentException("Base url should not be null or empty");
        }
        mOkHttpClient = new OkHttpClient();
        mRestAdapterBuilder = new Retrofit.Builder();

        mRestAdapterBuilder.client(mOkHttpClient);
        mRestAdapterBuilder.baseUrl(Utils.normalizeBaseUrl(baseUrl));

        Gson configuredGson = GsonFactory.create();
        mRestAdapterBuilder.addConverterFactory(GsonConverterFactory.create(configuredGson));
        mRestAdapterBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    @SuppressWarnings("unchecked")
    public SubBuilder setLog(RestApiLog log) {
        mLog = log;
        return (SubBuilder) this;
    }

    Retrofit.Builder getDefaultBuilder() {
        return mRestAdapterBuilder;
    }

    OkHttpClient getClient() {
        return mOkHttpClient;
    }

    abstract API createApi();

    public API build() {
        mOkHttpClient.interceptors().add(new LoggingInterceptor(mLog));
        return createApi();
    }
}
