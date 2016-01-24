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
import com.google.gson.stream.JsonReader;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class NetworkClient {
    private final OkHttpClient mClient;
    private final HttpUrl mBaseUrl;
    private final Gson mGson;

    NetworkClient(OkHttpClient client, HttpUrl baseUrl, Gson gson) {
        mClient = client;
        mBaseUrl = baseUrl;
        mGson = gson;
    }

    public OkHttpClient getClient() {
        return mClient;
    }

    public HttpUrl getBaseUrl() {
        return mBaseUrl;
    }

    public Gson getGson() {
        return mGson;
    }

    public <T> T deserializeJson(Response response, Class<T> type) throws IOException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(response.body().byteStream(), "UTF-8"));
        return mGson.fromJson(jsonReader, type);
    }

    public String deserializeString(Response response) throws IOException {
        return new String(response.body().bytes());
    }

    public Response makeCall(Request request) throws IOException, HttpException {
        com.squareup.okhttp.Call call1 = mClient.newCall(request);
        Response response = call1.execute();

        int code = response.code();
        if (code >= 200 && code < 300 || code == 302) {
            return response;
        } else {
            throw HttpException.httpError(response);
        }
    }

    public RequestBody createJsonRequestBody(Object body) {
        String json = mGson.toJson(body);
        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), json);
    }

    public static class Builder {
        private HttpUrl mBaseUrl;
        private OkHttpClient mClient;
        private Gson mGson;

        public Builder setBaseUrl(String url) {
            mBaseUrl = HttpUrl.parse(url);
            return this;
        }

        public Builder setClient(OkHttpClient client) {
            mClient = client;
            return this;
        }


        public Builder setGson(Gson gson) {
            mGson = gson;
            return this;
        }

        public NetworkClient build() {
            if (mBaseUrl == null) {
                throw new NullPointerException("base url should be supplied");
            }
            if (mClient == null) {
                mClient = new OkHttpClient();
            }
            if (mGson == null) {
                mGson = new Gson();
            }
            return new NetworkClient(mClient, mBaseUrl, mGson);
        }
    }
}
