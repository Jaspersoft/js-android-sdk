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

package com.jaspersoft.android.sdk.network;


import java.io.IOException;

import retrofit.Response;

/**
 * Wrapper around exceptions which could pop up during request processing.
 * Motivation behind class was to incapsulate 3-d party errors in generic interface.
 *
 * @author Tom Koptel
 * @since 2.0
 */
public class HttpException extends Exception {
    static HttpException networkError(IOException exception) {
        return new HttpException(exception.getMessage(), null,
                exception);
    }

    static HttpException httpError(Response response) {
        return httpError(response.raw());
    }

    static HttpException httpError(com.squareup.okhttp.Response response) {
        String message = response.code() + " " + response.message();
        return new HttpException(message, response, null);
    }

    private final com.squareup.okhttp.Response response;

    HttpException(String message, com.squareup.okhttp.Response response, Throwable exception) {
        super(message, exception);
        this.response = response;
    }

    // HTTP status code.
    public int code() {
        return response.code();
    }

    // HTTP status message.
    public String message() {
        return response.message();
    }

    public String errorBody() {
        return Utils.bodyToString(response.body());
    }

    public String urlString() {
        return response.request().urlString();
    }

}
