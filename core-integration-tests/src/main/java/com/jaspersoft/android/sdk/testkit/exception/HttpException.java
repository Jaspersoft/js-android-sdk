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

package com.jaspersoft.android.sdk.testkit.exception;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class HttpException extends Exception {
    private final int mCode;
    private final String mResponse;

    public HttpException(int code, String url, String response) {
        super(createMessage(code, url, response));
        mCode = code;
        mResponse = response;
    }

    private static String createMessage(int code, String url, String response) {
        String line = "===================================================";
        return String.format("\n\n%s\nRequest %s failed\ncode: %d\nmessage: %s\n%s\n",
                line, url, code, trim(response), line);
    }

    private static String trim(String response) {
        if (response.length() > 100) {
            return response.substring(0, 99);
        }
        return response;
    }

    public int getCode() {
        return mCode;
    }

    public String getResponse() {
        return mResponse;
    }

    public static class Factory {
        public static HttpException create(Response response) throws IOException, HttpException {
            int code = response.code();
            String url = response.request().urlString();
            String body = response.body().string();
            if (code == 401) {
                return AuthenticationException.create(url, body);
            } else if (code >= 400 && code < 500) {
                return new ClientErrorException(code, url, body);
            } else if (code >= 500 && code < 600) {
                return new ServerErrorException(code, url, body);
            } else {
                throw new RuntimeException("Unexpected response " + response);
            }
        }
    }
}
