/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.testkit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.jaspersoft.android.sdk.testkit.dto.Info;
import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class GetServerInfoUseCase extends HttpUseCase<Info, Void> {
    private final String mBaseUrl;

    protected GetServerInfoUseCase(OkHttpClient client, String baseUrl) {
        super(client);
        mBaseUrl = baseUrl;
    }

    @Override
    public Info execute(Void aVoid) throws IOException, HttpException {
        HttpUrl url = HttpUrl.parse(mBaseUrl).resolve("rest_v2/serverInfo");

        Response response = performRequest(url);

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream(), "utf-8"));
        return gson.fromJson(reader, Info.class);
    }
}
