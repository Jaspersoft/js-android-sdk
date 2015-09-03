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

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class BaseBuilder<API, SubBuilder> {
    private final Retrofit.Builder mRestAdapterBuilder;
    private final OkHttpClient mOkHttpClient;

    private RestApiLog mLog = RestApiLog.NONE;
    private RestApiLogLevel mLogLevel = RestApiLogLevel.NONE;
    public BaseBuilder(String baseUrl){
        if (baseUrl == null || baseUrl.length() == 0) {
            throw new IllegalArgumentException("Base url should not be null or empty");
        }
        mOkHttpClient = new OkHttpClient();
        mRestAdapterBuilder = new Retrofit.Builder();

        mRestAdapterBuilder.client(mOkHttpClient);
        mRestAdapterBuilder.baseUrl(baseUrl);

        Gson configuredGson = GsonFactory.create();
        mRestAdapterBuilder.addConverterFactory(GsonConverterFactory.create(configuredGson));

        /*
        TODO: Resolve error handling. It is still not clear what API will look like
         */
//        mRestAdapterBuilder.setErrorHandler(new retrofit.ErrorHandler() {
//            @Override
//            @SuppressWarnings("unchecked")
//            public Throwable handleError(RetrofitError cause) {
//                return ErrorHandler.DEFAULT.handleError(cause);
//            }
//        });
    }

    @SuppressWarnings("unchecked")
    public SubBuilder setLog(RestApiLog log) {
        mLog = log;
        return (SubBuilder) this;
    }

    @SuppressWarnings("unchecked")
    public SubBuilder setLogLevel(RestApiLogLevel logLevel) {
        mLogLevel = logLevel;
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
        /*
        TODO: Resolve log handling. Fallback to OkHttp interceptor https://github.com/square/okhttp/wiki/Interceptors
        */
//        mRestAdapterBuilder.setLog(new RetrofitLog(mLog));
//        mRestAdapterBuilder.setLogLevel(RestApiLogLevel.toRetrofitLog(mLogLevel));
        return createApi();
    }
}
