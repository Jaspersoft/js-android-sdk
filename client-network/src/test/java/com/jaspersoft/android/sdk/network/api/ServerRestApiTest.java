/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.MockResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerRestApiTest {

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    @Test
    public void shouldThroughRestErrorForHttpError() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(create500Response());

        ServerRestApi restApi = new ServerRestApi.Builder(mWebMockRule.getRootUrl()).build();
        restApi.requestServerInfo().toBlocking().first();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullBaseUrl() {
        mExpectedException.expect(IllegalArgumentException.class);
        new ServerRestApi.Builder(null).build();
    }

    private MockResponse create500Response() {
        return new MockResponse()
                .setStatus("HTTP/1.1 500 Internal Server Error");
    }
}
