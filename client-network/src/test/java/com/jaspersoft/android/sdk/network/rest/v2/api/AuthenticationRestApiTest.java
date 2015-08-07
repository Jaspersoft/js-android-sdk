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

import com.jaspersoft.android.sdk.network.rest.v2.entity.server.AuthResponse;
import com.jaspersoft.android.sdk.network.rest.v2.exception.RestError;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.MockResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.2
 */
public class AuthenticationRestApiTest {
    private final String LOCATION_SUCCESS = "/scripts/bower_components/js-sdk/src/common/auth/loginSuccess.json;jsessionid=7D86AE28407432B728694DF649DB5E8F";
    private final String LOCATION_ERROR = "login.html;jsessionid=395364A98787A1C42D5FEB0E9F58CF9F?error=1";

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private AuthenticationRestApi mRestApi;

    @Before
    public void setup() {
        mRestApi = new AuthenticationRestApi.Builder(mWebMockRule.getRootUrl()).build();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullBaseUrl() {
        mExpectedException.expect(IllegalArgumentException.class);
        new AuthenticationRestApi.Builder(null).build();
    }

    @Test
    public void shouldReturnResponseForSuccessRedirect() {
        MockResponse mockResponse = create302Response();
        mockResponse.addHeader("Location", mWebMockRule.getRootUrl() + LOCATION_SUCCESS);
        mWebMockRule.enqueue(mockResponse);

        AuthResponse response = mRestApi.authenticate("joeuser", "joeuser", "null", null);
        assertThat(response.getToken(), is(notNullValue()));
    }

    @Test
    public void shouldRiseErrorForErrorRedirect() {
        mExpectedException.expect(RestError.class);

        MockResponse mockResponse = create302Response();
        mockResponse.addHeader("Location", mWebMockRule.getRootUrl() + LOCATION_ERROR);
        mWebMockRule.enqueue(mockResponse);

        mRestApi.authenticate("joeuser", "joeuser", "null", null);
    }

    @Test
    public void shouldRiseErrorForHttpException() {
        mExpectedException.expect(RestError.class);

        MockResponse mockResponse = create500Response();
        mWebMockRule.enqueue(mockResponse);

        mRestApi.authenticate("joeuser", "joeuser", "null", null);
    }

    @Test
    public void shouldRiseIllegalExceptionIfLocationHeaderIsMissing() {
        mExpectedException.expect(IllegalStateException.class);
        mExpectedException.expectMessage("Missing 'Location' header");

        MockResponse mockResponse = create302Response();
        mWebMockRule.enqueue(mockResponse);

        mRestApi.authenticate("joeuser", "joeuser", "null", null);
    }

    private MockResponse create302Response() {
        return new MockResponse()
                .addHeader("Set-Cookie", "cookie1")
                .setStatus("HTTP/1.1 302 Found");
    }

    private MockResponse create500Response() {
        return new MockResponse()
                .setStatus("HTTP/1.1 500 Internal Server Error");
    }
}
