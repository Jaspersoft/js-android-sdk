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

import com.google.gson.stream.JsonReader;
import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class GetReportParametersUseCase extends HttpUseCase< Map<String, Set<String>>, ListReportParamsCommand> {
    private final String mBaseUrl;

    public GetReportParametersUseCase(OkHttpClient client, String baseUrl) {
        super(client);
        mBaseUrl = baseUrl;
    }

    @Override
    public Map<String, Set<String>> execute(ListReportParamsCommand command) throws IOException, HttpException {
        String path = String.format("rest_v2/reports%s/inputControls/values", command.getReportUri());
        HttpUrl url = HttpUrl.parse(mBaseUrl + path).newBuilder()
                .addQueryParameter("freshData", "true")
                .build();

        Response response = performRequest(url);
        Map<String, Set<String>> params = new HashMap<>();
        mapResponse(response, params);
        return params;
    }

    private void mapResponse(Response response, Map<String, Set<String>> params) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
        reader.beginObject();
        reader.nextName();
        reader.beginArray();
        while (reader.hasNext()) {
            populateParams(params, reader);
        }
        reader.endArray();
        reader.endObject();
    }

    private void populateParams(Map<String, Set<String>> params, JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            Set<String> values = new HashSet<>();
            String id = "null";

            String name = reader.nextName();
            if ("options".equals(name)) {
                readValues(reader, values);
            } else if ("id".equals(name)) {
                id = reader.nextString();
            } else {
                reader.skipValue();
            }
            params.put(id, values);
        }
        reader.endObject();
    }

    private void readValues(JsonReader reader, Set<String> values) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if ("value".equals(name)) {
                    values.add(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        reader.endArray();
    }
}
