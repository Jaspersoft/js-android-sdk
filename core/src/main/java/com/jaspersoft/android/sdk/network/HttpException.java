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


import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;
import com.squareup.okhttp.ResponseBody;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Wrapper around exceptions which could pop up during request processing.
 * Motivation behind class was to encapsulate 3-d party errors in generic interface.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class HttpException extends Exception {
    private final int mCode;
    private final ResponseBody mBody;

    private ErrorDescriptor mDescriptor;

    HttpException(String responseMessage, int code, ResponseBody responseBody) {
        super(responseMessage + " " + code);
        mCode = code;
        mBody = responseBody;
    }

    static HttpException httpError(com.squareup.okhttp.Response response) {
        return new HttpException(response.message(), response.code(), response.body());
    }

    public int code() {
        return mCode;
    }

    @Nullable
    public ErrorDescriptor getDescriptor() throws IOException {
        if (mDescriptor == null) {
            mDescriptor = parseErrorDescriptor();
        }
        return mDescriptor;
    }

    private ErrorDescriptor parseErrorDescriptor() throws IOException {
        Gson gson = GsonFactory.create();
        InputStream stream = mBody.byteStream();
        InputStreamReader reader = new InputStreamReader(stream);
        try {
            return gson.fromJson(reader, ErrorDescriptor.class);
        } catch (JsonParseException ex) {
            return null;
        }
    }
}
