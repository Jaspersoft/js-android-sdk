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

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import org.jetbrains.annotations.NotNull;
import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ServerRestApiImpl implements ServerRestApi {

    private final Client mClient;

    private RestApi mApi;

    public ServerRestApiImpl(Client client) {
        mClient = client;
    }

    @NotNull
    @Override
    public ServerInfoData requestServerInfo() throws IOException, HttpException {
        Call<ServerInfoData> call = getApi().requestServerInfo();
        return CallWrapper.wrap(call).body();
    }

    @NotNull
    @Override
    public String requestEdition() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestEdition()).body();
    }

    @NotNull
    @Override
    public String requestVersion() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestVersion()).body();
    }

    @NotNull
    @Override
    public String requestBuild() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestBuild()).body();
    }

    @NotNull
    @Override
    public String requestFeatures() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestFeatures()).body();
    }

    @NotNull
    @Override
    public String requestEditionName() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestEditionName()).body();
    }

    @NotNull
    @Override
    public String requestLicenseType() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestLicenseType()).body();
    }

    @NotNull
    @Override
    public String requestExpiration() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestExpiration()).body();
    }

    @NotNull
    @Override
    public String requestDateFormatPattern() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestDateFormatPattern()).body();
    }

    @NotNull
    @Override
    public String requestDateTimeFormatPattern() throws IOException, HttpException {
        return CallWrapper.wrap(getApi().requestDateTimeFormatPattern()).body();
    }

    private RestApi getApi() {
        if (mApi == null) {
            RetrofitFactory retrofitFactory = mClient.getRetrofitFactory();
            Retrofit retrofit = retrofitFactory.newRetrofit().build();
            return retrofit.create(RestApi.class);
        }
        return mApi;
    }

    private interface RestApi {
        @NotNull
        @Headers("Accept: application/json")
        @GET(value = "rest_v2/serverInfo")
        Call<ServerInfoData> requestServerInfo();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/edition")
        Call<String> requestEdition();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/version")
        Call<String> requestVersion();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/build")
        Call<String> requestBuild();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/features")
        Call<String> requestFeatures();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/editionName")
        Call<String> requestEditionName();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/licenseType")
        Call<String> requestLicenseType();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/expiration")
        Call<String> requestExpiration();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/dateFormatPattern")
        Call<String> requestDateFormatPattern();

        @Headers("Accept: text/plain")
        @GET(value = "rest_v2/serverInfo/datetimeFormatPattern")
        Call<String> requestDateTimeFormatPattern();
    }
}
