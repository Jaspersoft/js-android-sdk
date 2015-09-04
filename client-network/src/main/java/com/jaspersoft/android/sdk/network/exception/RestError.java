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

package com.jaspersoft.android.sdk.network.exception;


//import retrofit.RetrofitError;

import java.io.IOException;

/**
 * TODO we need resolve error issues
 *
 * @author Tom Koptel
 * @since 2.0
 */
public final class RestError extends RuntimeException {
    public static RestError networkError(String url, IOException exception) {
        return new RestError(exception.getMessage(), url, null, Kind.NETWORK,
                exception);
    }

    public static RestError httpError(String url, com.squareup.okhttp.Response response) {
        String message = response.code() + " " + response.message();
        return new RestError(message, url, response, Kind.HTTP, null);
    }

    public static RestError unexpectedError(String url, Throwable exception) {
        return new RestError(exception.getMessage(), url, null, Kind.UNEXPECTED,
                exception);
    }

    private final String url;
    private final com.squareup.okhttp.Response response;
    private final Kind kind;

    RestError(String message, String url, com.squareup.okhttp.Response response,Kind kind, Throwable exception) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
    }

    /** HTTP status code. */
    public int code() {
        return response.code();
    }

    /** HTTP status message. */
    public String message() {
        return response.message();
    }

    public com.squareup.okhttp.Response response() {
        return response;
    }

    public String urlString() {
        return url;
    }

    public Kind kind() {
        return kind;
    }

    public enum Kind {
        NETWORK,
        HTTP,
        UNEXPECTED;

    }

}
