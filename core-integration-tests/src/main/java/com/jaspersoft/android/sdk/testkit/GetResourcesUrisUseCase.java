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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class GetResourcesUrisUseCase extends HttpUseCase<List<String>, ListResourcesUrisCommand> {
    private final String mBaseUrl;

    GetResourcesUrisUseCase(OkHttpClient client, String baseUrl) {
        super(client);
        mBaseUrl = baseUrl;
    }

    @Override
    public List<String> execute(ListResourcesUrisCommand command) throws IOException, HttpException {
        HttpUrl httpUrl = HttpUrl.parse(mBaseUrl + "rest_v2/resources").newBuilder()
                .addQueryParameter("forceFullPage", "true")
                .addQueryParameter("limit", String.valueOf(command.getCount()))
                .addQueryParameter("offset", "0")
                .addQueryParameter("type", command.getType())
                .build();

        Response response = performRequest(httpUrl);
        List<String> resources = new ArrayList<>();
        JsonReader reader = new JsonReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));

        reader.beginObject();
        reader.nextName();
        reader.beginArray();

        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if ("uri".equals(name)) {
                    resources.add(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }

        reader.endArray();
        reader.endObject();

        return Collections.unmodifiableList(resources);
    }
}
