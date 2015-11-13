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

package com.jaspersoft.android.sdk.testkit;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jaspersoft.android.sdk.testkit.dto.ReportExecConfig;
import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ReportExec {
    private final String mToken;
    private final String mBaseUrl;
    private final OkHttpClient mOkhttp;

    ReportExec(String token, String baseUrl) {
        mToken = token;
        mBaseUrl = baseUrl;
        mOkhttp = new OkHttpClient();
    }

    public String start(ReportExecConfig config) throws IOException, HttpException {
        HttpUrl httpUrl = HttpUrl.parse(mBaseUrl + "rest_v2/reportExecutions");
        MediaType applicationJson = MediaType.parse("application/json; charset=utf-8");

        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(applicationJson, gson.toJson(config));
        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Cookie", mToken)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        Response response = CallAdapter.adapt(mOkhttp.newCall(request));

        JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if ("requestId".equals(name)) {
                return reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        throw new RuntimeException("Failed to receive 'requestId'");
    }

    public boolean cancel(String executionId) throws IOException, HttpException {
        String path = String.format("rest_v2/reportExecutions/%s/status", executionId);
        HttpUrl httpUrl = HttpUrl.parse(mBaseUrl + path);
        MediaType applicationJson = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(applicationJson, "{\"value\": \"cancelled\"}");
        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Cookie", mToken)
                .addHeader("Accept", "application/json")
                .put(requestBody)
                .build();

        Response response = CallAdapter.adapt(mOkhttp.newCall(request));
        return response.code() != 204;
    }
}
