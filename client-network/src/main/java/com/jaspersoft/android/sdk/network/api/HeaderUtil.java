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

package com.jaspersoft.android.sdk.network.api;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Header;
import retrofit.client.Response;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class HeaderUtil {
    private final List<Header> mHeaders;

    HeaderUtil(@NonNull List<Header> headers) {
        mHeaders = headers;
    }

    public static HeaderUtil wrap(@NonNull Response response) {
        return new HeaderUtil(response.getHeaders());
    }

    @NonNull
    public SafeHeader getFirstHeader(String name) {
        List<Header> headers = findHeaders(name);
        if (headers.isEmpty()) {
            return new SafeHeader(null);
        } else {
            return new SafeHeader(headers.get(0));
        }
    }

    private List<Header> findHeaders(String name) {
        List<Header> result = new ArrayList<>();
        for (Header header : mHeaders) {
            if (header.getName().equals(name)) {
                result.add(header);
            }
        }
        return result;
    }
}
