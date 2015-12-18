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

package com.jaspersoft.android.sdk.network;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;
import com.squareup.okhttp.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit.Retrofit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class AnonymousClient {
    private final OkHttpClient mOkHttpClient;
    private final String mBaseUrl;

    private Retrofit mRetrofit;

    AnonymousClient(String baseUrl, OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
        mBaseUrl = baseUrl;
    }

    OkHttpClient getClient() {
        return mOkHttpClient;
    }

    String getBaseUrl() {
        return mBaseUrl;
    }

    Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = configureRetrofit();
        }
        return mRetrofit;
    }

    private Retrofit configureRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(mBaseUrl);

        Gson configuredGson = GsonFactory.create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(configuredGson);
        StringConverterFactory stringConverterFactory = StringConverterFactory.create();

        builder.addConverterFactory(gsonConverterFactory);
        builder.addConverterFactory(stringConverterFactory);

        return builder.build();
    }

    @NotNull
    public final ServerRestApi infoApi() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
