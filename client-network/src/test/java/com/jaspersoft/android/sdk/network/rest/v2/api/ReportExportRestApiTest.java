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

import com.jaspersoft.android.sdk.network.rest.v2.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.rest.v2.entity.export.ExportResourceResponse;
import com.jaspersoft.android.sdk.network.rest.v2.exception.RestError;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.MockResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportRestApiTest {

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private ReportExportRestApi restApiUnderTest;

    @Before
    public void setup() {
        restApiUnderTest = new ReportExportRestApi.Builder(mWebMockRule.getRootUrl(), "cookie").build();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullBaseUrl() {
        mExpectedException.expect(IllegalArgumentException.class);
        new ReportExportRestApi.Builder(null, "cookie").build();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullCookie() {
        mExpectedException.expect(IllegalArgumentException.class);
        new ReportExportRestApi.Builder(mWebMockRule.getRootUrl(), null).build();
    }

    @Test
    public void shouldThroughRestErrorOnSearchRequestIfHttpError() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(create500Response());

        restApiUnderTest.runExecution("any_id", ExecutionRequestOptions.newInstance());
    }

    @Test
    public void pathParameterShouldNotBeNullForRunRequestExecution() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Path parameter \"executionId\" value must not be null.");

        restApiUnderTest.runExecution(null, ExecutionRequestOptions.newInstance());
    }

    @Test
    public void bodyShouldNotBeNullForRunRequestExecution() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Body parameter value must not be null.");

        restApiUnderTest.runExecution("any_id", null);
    }

    @Test
    public void pathParameter1ShouldNotBeNullForCheckRequestExecutionStatus() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Path parameter \"executionId\" value must not be null.");

        restApiUnderTest.checkExecutionStatus(null, "any_id");
    }

    @Test
    public void pathParameter2ShouldNotBeNullForCheckRequestExecutionStatus() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Path parameter \"exportId\" value must not be null.");

        restApiUnderTest.checkExecutionStatus("any_id", null);
    }

    @Test
    public void requestForOutputShouldParsePagesFromHeader() {
        MockResponse mockResponse = create200Response()
                .setBody("")
                .addHeader("report-pages", "1-10");
        mWebMockRule.enqueue(mockResponse);

        ExportResourceResponse resource = restApiUnderTest.requestOutput("any_id", "any_id");
        assertThat(resource.getPages(), is("1-10"));
    }

    @Test
    public void requestForOutputShouldParseIsFinalHeader() {
        MockResponse mockResponse = create200Response()
                .setBody("")
                .addHeader("output-final", "true");
        mWebMockRule.enqueue(mockResponse);

        ExportResourceResponse resource = restApiUnderTest.requestOutput("any_id", "any_id");
        assertThat(resource.isFinal(), is(true));
    }

    private MockResponse create200Response() {
        return new MockResponse()
                .setStatus("HTTP/1.1 200 Ok");
    }

    private MockResponse create500Response() {
        return new MockResponse()
                .setStatus("HTTP/1.1 500 Internal Server Error");
    }
}
