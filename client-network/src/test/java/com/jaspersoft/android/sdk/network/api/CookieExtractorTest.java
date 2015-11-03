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

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class CookieExtractorTest {

    private Request mRequest;

    @Before
    public void setup() {
        mRequest = new Request.Builder()
                .url("http://localhost")
                .build();
    }

    @Test
    public void shouldExtractTokenFromNetworkResponse() {
        Response mockResponse = new Response.Builder()
                .addHeader("Set-Cookie", "cookie1")
                .addHeader("Set-Cookie", "cookie2")
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .request(mRequest)
                .build();

        String token = CookieExtractor.extract(mockResponse);
        assertThat(token, is("cookie1;cookie2"));
    }
}
