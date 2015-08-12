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

import retrofit.RestAdapter;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public enum RestApiLogLevel {
    /** No logging. */
    NONE,
    /** Log only the request method and URL and the response status code and execution time. */
    BASIC,
    /** Log the basic information along with request and response headers. */
    HEADERS,
    /** Log the basic information along with request and response objects via toString(). */
    HEADERS_AND_ARGS,
    /**
     * Log the headers, body, and metadata for both requests and responses.
     * <p>
     * Note: This requires that the entire request and response body be buffered in memory!
     */
    FULL;

    static RestAdapter.LogLevel toRetrofitLog(RestApiLogLevel logLevel) {
        return RestAdapter.LogLevel.valueOf(logLevel.name());
    }
}
