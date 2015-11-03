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

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;

import org.jetbrains.annotations.NotNull;

import retrofit.Call;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ServerRestApiImpl implements ServerRestApi {

    private final RestApi mApi;

    public ServerRestApiImpl(Retrofit retrofit) {
        mApi = retrofit.create(RestApi.class);
    }

    @NotNull
    @Override
    public ServerInfoData requestServerInfo() {
        Call<ServerInfoData> call = mApi.requestServerInfo();
        return CallWrapper.wrap(call).body();
    }

    @NotNull
    @Override
    public String requestEdition() {
        return CallWrapper.wrap(mApi.requestEdition()).body();
    }

    @NotNull
    @Override
    public String requestVersion() {
        return CallWrapper.wrap(mApi.requestVersion()).body();
    }

    @NotNull
    @Override
    public String requestBuild() {
        return CallWrapper.wrap(mApi.requestBuild()).body();
    }

    @NotNull
    @Override
    public String requestFeatures() {
        return CallWrapper.wrap(mApi.requestFeatures()).body();
    }

    @NotNull
    @Override
    public String requestEditionName() {
        return CallWrapper.wrap(mApi.requestEditionName()).body();
    }

    @NotNull
    @Override
    public String requestLicenseType() {
        return CallWrapper.wrap(mApi.requestLicenseType()).body();
    }

    @NotNull
    @Override
    public String requestExpiration() {
        return CallWrapper.wrap(mApi.requestExpiration()).body();
    }

    @NotNull
    @Override
    public String requestDateFormatPattern() {
        return CallWrapper.wrap(mApi.requestDateFormatPattern()).body();
    }

    @NotNull
    @Override
    public String requestDateTimeFormatPattern() {
        return CallWrapper.wrap(mApi.requestDateTimeFormatPattern()).body();
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
