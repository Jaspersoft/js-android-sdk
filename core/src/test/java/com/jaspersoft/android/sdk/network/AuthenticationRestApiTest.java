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

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.RestError;
import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
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
 * @since 2.0
 */
public class AuthenticationRestApiTest {
    private final String LOCATION_SUCCESS = "/scripts/bower_components/js-sdk/src/common/auth/loginSuccess.json;jsessionid=7D86AE28407432B728694DF649DB5E8F";
    private final String LOCATION_ERROR = "login.html;jsessionid=395364A98787A1C42D5FEB0E9F58CF9F?error=1";

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private AuthenticationRestApi mRestApi;

    @ResourceFile("json/encryption_key.json")
    TestResource mKey;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        mRestApi = new AuthenticationRestApi.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void shouldReturnResponseForSuccessRedirect() {
        MockResponse mockResponse = MockResponseFactory.create302()
                .addHeader("Set-Cookie", "cookie1")
                .addHeader("Location", mWebMockRule.getRootUrl() + LOCATION_SUCCESS);
        mWebMockRule.enqueue(mockResponse);

        String response = mRestApi.authenticate("joeuser", "joeuser", null, null);
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void shouldRiseErrorForErrorRedirect() {
        mExpectedException.expect(RestError.class);

        MockResponse mockResponse = MockResponseFactory.create302()
                .addHeader("Location", mWebMockRule.getRootUrl() + LOCATION_ERROR)
                .addHeader("Set-Cookie", "cookie1");
        mWebMockRule.enqueue(mockResponse);

        mRestApi.authenticate("joeuser", "joeuser", "null", null);
    }

    @Test
    public void shouldRiseErrorForHttpException() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        mRestApi.authenticate("joeuser", "joeuser", "null", null);
    }

    @Test
    public void shouldRiseIllegalExceptionIfLocationHeaderIsMissing() {
        mExpectedException.expect(IllegalStateException.class);
        mExpectedException.expectMessage("Location HEADER is missing please contact JRS admin");

        mWebMockRule.enqueue(MockResponseFactory.create302().addHeader("Set-Cookie", "cookie1"));

        mRestApi.authenticate("joeuser", "joeuser", "null", null);
    }

    @Test
    public void shouldReturnEncryptionKeyIfApiAvailable() {
        MockResponse anonymousCookie = MockResponseFactory.create200()
                .setBody("6.1")
                .addHeader("Set-Cookie", "cookie1");
        MockResponse encryptionKey = MockResponseFactory.create200()
                .setBody(mKey.asString());
        mWebMockRule.enqueue(anonymousCookie);
        mWebMockRule.enqueue(encryptionKey);

        EncryptionKey keyResponse = mRestApi.requestEncryptionMetadata();
        assertThat(keyResponse, is(notNullValue()));
    }

    @Test
    public void shouldReturnEmptyEncryptionKeyIfApiNotAvailable() {
        MockResponse anonymousCookie = MockResponseFactory.create200()
                .setBody("6.1")
                .addHeader("Set-Cookie", "cookie1");

        String malformedJson = "{Error: Key generation is off}";
        MockResponse encryptionKey = MockResponseFactory.create200()
                .setBody(malformedJson);

        mWebMockRule.enqueue(anonymousCookie);
        mWebMockRule.enqueue(encryptionKey);

        EncryptionKey keyResponse = mRestApi.requestEncryptionMetadata();
        assertThat(keyResponse.isAvailable(), is(false));
    }
}
