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

package com.jaspersoft.android.sdk.test;

import com.squareup.okhttp.mockwebserver.MockResponse;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class MockResponseFactory {

    private MockResponseFactory() {}

    public static MockResponse create200() {
        return new MockResponse()
                .setStatus("HTTP/1.1 200 Ok");
    }

    public static MockResponse create204() {
        return new MockResponse()
                .setStatus("HTTP/1.1 204 No Content");
    }

    public static MockResponse create500() {
        return new MockResponse()
                .setStatus("HTTP/1.1 500 Internal Server Error");
    }

    public static MockResponse create302() {
        return new MockResponse()
                .setStatus("HTTP/1.1 302 Found");
    }

    public static MockResponse create401() {
        return new MockResponse()
                .setStatus("HTTP/1.1 401 Unauthorized");
    }
}
