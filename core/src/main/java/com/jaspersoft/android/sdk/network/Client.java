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

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Client {
    private final String mBaseUrl;

    private RetrofitFactory mRetrofitFactory;
    private HttpClientFactory mHttpClientFactory;

    public Client(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public ServerRestApi infoApi() {
        return new ServerRestApiImpl(this);
    }

    public static GenericBuilder newBuilder() {
        return new GenericBuilder();
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    RetrofitFactory getRetrofitFactory() {
        if (mRetrofitFactory == null) {
            mRetrofitFactory = new RetrofitFactory(this);
        }
        return mRetrofitFactory;
    }

    HttpClientFactory getClientFactory() {
        if (mHttpClientFactory == null) {
            mHttpClientFactory = new HttpClientFactory(this);
        }
        return mHttpClientFactory;
    }

    public static class GenericBuilder {
        public OptionalBuilder withBaseUrl(String baseUrl) {
            return new OptionalBuilder(baseUrl);
        }
    }

    public static class OptionalBuilder {
        private final String mBaseUrl;

        private OptionalBuilder(String baseUrl) {
            mBaseUrl = baseUrl;
        }

        public Client create() {
            return new Client(mBaseUrl);
        }
    }
}
