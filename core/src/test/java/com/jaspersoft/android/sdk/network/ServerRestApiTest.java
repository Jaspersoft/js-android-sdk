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

package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasPath.hasPath;
import static com.jaspersoft.android.sdk.test.matcher.IsRecorderRequestContainsHeader.containsHeader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ServerRestApiTest {

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private ServerRestApi objectUnderTest;

    @Before
    public void setup() {
        Server server = Server.builder()
                .withBaseUrl(mWebMockRule.getRootUrl())
                .build();
        NetworkClient networkClient = server.newNetworkClient().build();
        objectUnderTest = new ServerRestApi(networkClient);
    }

    @Test
    public void shouldThroughRestErrorForHttpError() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        objectUnderTest.requestServerInfo();
    }

    @Test
    public void shouldBuildAppropriateJsonRequestForServerInfo() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("{}")
        );

        ServerInfoData serverInfoData = objectUnderTest.requestServerInfo();
        assertThat("Response not null value", serverInfoData, is(notNullValue()));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept json header", recordedRequest, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo'", recordedRequest, hasPath("/rest_v2/serverInfo"));
    }

    @Test
    public void shouldHandleEmptyTextResponse() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create200().setBody(""));

        objectUnderTest.requestServerInfo();
    }

    @Test
    public void shouldHandlePlainTextResponseForBuild() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Enterprise for AWS")
        );

        String editionName = objectUnderTest.requestBuild();
        assertThat(editionName, is("Enterprise for AWS"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/build'", recordedRequest, hasPath("/rest_v2/serverInfo/build"));
    }

    @Test
    public void shouldHandlePlainTextResponseForDateFormatPattern() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("yyyy-MM-dd")
        );
        String editionName = objectUnderTest.requestDateFormatPattern();
        assertThat(editionName, is("yyyy-MM-dd"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/dateFormatPattern'", recordedRequest, hasPath("/rest_v2/serverInfo/dateFormatPattern"));
    }

    @Test
    public void shouldHandlePlainTextResponseForDatetimeFormatPattern() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("yyyy-MM-dd'T'HH:mm:ss")
        );
        String editionName = objectUnderTest.requestDateTimeFormatPattern();
        assertThat(editionName, is("yyyy-MM-dd'T'HH:mm:ss"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/datetimeFormatPattern'", recordedRequest, hasPath("/rest_v2/serverInfo/datetimeFormatPattern"));
    }

    @Test
    public void shouldHandlePlainTextResponseForEdition() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("PRO")
        );
        String editionName = objectUnderTest.requestEdition();
        assertThat(editionName, is("PRO"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/edition'", recordedRequest, hasPath("/rest_v2/serverInfo/edition"));
    }


    @Test
    public void shouldHandlePlainTextResponseForEditionName() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Enterprise for AWS")
        );
        String editionName = objectUnderTest.requestEditionName();
        assertThat(editionName, is("Enterprise for AWS"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/editionName'", recordedRequest, hasPath("/rest_v2/serverInfo/editionName"));
    }

    @Test
    public void shouldHandlePlainTextResponseForExpiration() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("1000")
        );
        String editionName = objectUnderTest.requestExpiration();
        assertThat(editionName, is("1000"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/expiration'", recordedRequest, hasPath("/rest_v2/serverInfo/expiration"));
    }

    @Test
    public void shouldHandlePlainTextResponseForFeatures() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Fusion AHD EXP DB ANA AUD MT ")
        );
        String editionName = objectUnderTest.requestFeatures();
        assertThat(editionName, is("Fusion AHD EXP DB ANA AUD MT "));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/features'", recordedRequest, hasPath("/rest_v2/serverInfo/features"));
    }

    @Test
    public void shouldHandlePlainTextResponseForLicenseType() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("Commercial")
        );
        String editionName = objectUnderTest.requestLicenseType();
        assertThat(editionName, is("Commercial"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/licenseType'", recordedRequest, hasPath("/rest_v2/serverInfo/licenseType"));
    }

    @Test
    public void shouldHandlePlainTextResponseFor() throws Exception {
        mWebMockRule.enqueue(
                MockResponseFactory.create200().setBody("5.5.0")
        );
        String editionName = objectUnderTest.requestVersion();
        assertThat(editionName, is("5.5.0"));

        RecordedRequest recordedRequest = mWebMockRule.get().takeRequest();
        assertThat("Should accept text plain header", recordedRequest, containsHeader("Accept", "text/plain; charset=UTF-8"));
        assertThat("Should have path '/rest_v2/serverInfo/version'", recordedRequest, hasPath("/rest_v2/serverInfo/version"));
    }
}
