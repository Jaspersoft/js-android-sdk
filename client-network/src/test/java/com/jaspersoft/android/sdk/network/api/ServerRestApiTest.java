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

import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerRestApiTest {

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private ServerRestApi objectUnderTest;

    @Before
    public void setup() {
        objectUnderTest = new ServerRestApi.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void shouldThroughRestErrorForHttpError() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        objectUnderTest.requestServerInfo();
    }

    @Test
    public void shouldHandlePlainTextResponseForBuild() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Enterprise for AWS")
        );
        String editionName = objectUnderTest.requestBuild();
        assertThat(editionName, is("Enterprise for AWS"));

        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/build"));
    }

    @Test
    public void shouldHandlePlainTextResponseForDateFormatPattern() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("yyyy-MM-dd")
        );
        String editionName = objectUnderTest.requestDateFormatPattern();
        assertThat(editionName, is("yyyy-MM-dd"));


        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/dateFormatPattern"));
    }

    @Test
    public void shouldHandlePlainTextResponseForDatetimeFormatPattern() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("yyyy-MM-dd'T'HH:mm:ss")
        );
        String editionName = objectUnderTest.requestDateTimeFormatPattern();
        assertThat(editionName, is("yyyy-MM-dd'T'HH:mm:ss"));


        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/datetimeFormatPattern"));
    }

    @Test
    public void shouldHandlePlainTextResponseForEdition() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("PRO")
        );
        String editionName = objectUnderTest.requestEdition();
        assertThat(editionName, is("PRO"));

        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/edition"));
    }


    @Test
    public void shouldHandlePlainTextResponseForEditionName() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Enterprise for AWS")
        );
        String editionName = objectUnderTest.requestEditionName();
        assertThat(editionName, is("Enterprise for AWS"));

        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/editionName"));
    }

    @Test
    public void shouldHandlePlainTextResponseForExpiration() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("1000")
        );
        String editionName = objectUnderTest.requestExpiration();
        assertThat(editionName, is("1000"));

        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/expiration"));
    }

    @Test
    public void shouldHandlePlainTextResponseForFeatures() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Fusion AHD EXP DB ANA AUD MT ")
        );
        String editionName = objectUnderTest.requestFeatures();
        assertThat(editionName, is("Fusion AHD EXP DB ANA AUD MT "));

        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/features"));
    }

    @Test
    public void shouldHandlePlainTextResponseForLicenseType() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Commercial")
        );
        String editionName = objectUnderTest.requestLicenseType();
        assertThat(editionName, is("Commercial"));

        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/licenseType"));
    }

    @Test
    public void shouldHandlePlainTextResponseFor() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("5.5.0")
        );
        String editionName = objectUnderTest.requestVersion();
        assertThat(editionName, is("5.5.0"));

        String path = mWebMockRule.get().takeRequest().getPath();
        assertThat(path, is("/rest_v2/serverInfo/version"));
    }
}
