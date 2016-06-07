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

package com.jaspersoft.android.sdk.network;

import com.squareup.okhttp.HttpUrl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.3
 */
enum QueryMapper {
    INSTANCE;

    public HttpUrl mapParams(Map<String, Object> searchParams, HttpUrl url) throws IOException {
        if (searchParams == null) {
            searchParams = Collections.emptyMap();
        }

        HttpUrl.Builder urlBuilder = url.newBuilder();

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();

            if (value instanceof Iterable<?>) {
                Iterable<?> iterable = (Iterable<?>) value;
                for (Object v : iterable) {
                    String encodedValue = encodeValue(v);
                    urlBuilder.addEncodedQueryParameter(key, encodedValue);
                }
            } else {
                String encodedValue = encodeValue(value);
                urlBuilder.addEncodedQueryParameter(key, encodedValue);
            }
        }

        return urlBuilder.build();
    }

    private String encodeValue(Object value) throws UnsupportedEncodingException {
        String rawValue = String.valueOf(value);
        return URLEncoder.encode(rawValue, "UTF-8");
    }
}
