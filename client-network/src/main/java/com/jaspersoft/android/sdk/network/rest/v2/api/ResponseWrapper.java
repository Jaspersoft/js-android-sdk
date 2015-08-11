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

package com.jaspersoft.android.sdk.network.rest.v2.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit.client.Header;
import retrofit.mime.TypedInput;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ResponseWrapper<Response> {

    private final retrofit.client.Response mResponse;
    private final Type mType;
    private final Gson mGson;

    private ResponseWrapper(retrofit.client.Response response, Type type) {
        mResponse = response;
        mType = type;
        mGson = GsonFactory.create();
    }

    @SuppressWarnings("unchecked")
    public static <T> ResponseWrapper<T> wrap(retrofit.client.Response response, Type type) {
        return new ResponseWrapper(response, type);
    }

    @NonNull
    public Response parseResponse() {
        TypedInput typedInput = mResponse.getBody();
        Reader reader = null;
        try {
            reader = new InputStreamReader(typedInput.in());
        } catch (IOException e) {
            // TODO it is no good idea to rethrow it here. Better solution to wrap with RestError
            throw new RuntimeException(e);
        }
        return mGson.fromJson(reader, mType);
    }

    @Nullable
    public Header getFirstHeader(String name) {
        List<Header> headers = findHeaders(name);
        if (headers.isEmpty()) {
            return null;
        } else {
            Header header = headers.get(0);
            if (header == null) {
                return null;
            }
            return header;
        }
    }

    private List<Header> findHeaders(String name) {
        List<Header> result = new ArrayList<>();
        List<Header> headers = mResponse.getHeaders();
        for (Header header : headers) {
            if (header.getName().equals(name)) {
                result.add(header);
            }
        }
        return result;
    }

}
