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

package com.jaspersoft.android.sdk.client.api.v2;

import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;

import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ServerRestApi {

    @GET(value = "/rest_v2/serverInfo")
    ServerInfo getServerInfo();

    class Builder {
        private final String mBaseUrl;
        private boolean mXmlDataType;

        Builder(@Nullable String baseUrl) {
            mBaseUrl = baseUrl;
            mXmlDataType = true;
        }

        public Builder useXmlDataType() {
            mXmlDataType = true;
            return this;
        }

        public Builder useJsonDataType() {
            mXmlDataType = false;
            return this;
        }

        public ServerRestApi build() {
            Endpoint endpoint = Endpoints.newFixedEndpoint(mBaseUrl);

            RestAdapter.Builder builder = new RestAdapter.Builder();
            builder.setRequestInterceptor(mXmlDataType ? new XmlHttpRequestInterceptor() : new JsonHttpRequestInterceptor());
//            builder.setConverter(mXmlDataType ? new SimpleXMLConverter() : new JsonHttpRequestInterceptor());
            builder.setEndpoint(endpoint);
            RestAdapter restAdapter = builder.build();

            return restAdapter.create(ServerRestApi.class);
        }
    }
}
