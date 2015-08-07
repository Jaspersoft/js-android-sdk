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

package com.jaspersoft.android.sdk.network.rest.v2.server;

import com.jaspersoft.android.sdk.network.rest.v2.entity.server.AuthResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.client.Header;
import retrofit.client.Response;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class AuthResponseFactory {
    private final List<String> mCookieParts;

    AuthResponseFactory(List<String> parts) {
        mCookieParts = parts;
    }

    public static AuthResponse create(Response response) {
        List<Header> headers = response.getHeaders();
        List<String> parts = new ArrayList<>();
        for (Header header : headers) {
            if (header.getName().equals("Set-Cookie")) {
                parts.add(header.getValue());
            }
        }
        AuthResponseFactory responseFactory = new AuthResponseFactory(parts);
        return responseFactory.create();
    }

    private AuthResponse create() {
        String cookie = joinCookieParts().toString();
        return new AuthResponse(cookie);
    }

    private StringBuilder joinCookieParts() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = mCookieParts.iterator();
        while (iterator.hasNext()) {
            String cookie = iterator.next();
            stringBuilder.append(cookie);
            if (iterator.hasNext()) {
                stringBuilder.append(";");
            }
        }
        return stringBuilder;
    }
}
