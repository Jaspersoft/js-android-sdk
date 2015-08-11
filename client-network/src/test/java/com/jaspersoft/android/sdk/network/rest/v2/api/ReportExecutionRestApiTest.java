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
import com.jaspersoft.android.sdk.network.rest.v2.exception.RestError;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
import com.squareup.okhttp.mockwebserver.MockResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExecutionRestApiTest {

    @ResourceFile("json/cancelled_report_response.json")
    TestResource cancelledResponse;

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();
    private ReportExecutionRestApi restApiUnderTest;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        restApiUnderTest = new ReportExecutionRestApi.Builder(mWebMockRule.getRootUrl(), "cookie").build();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullBaseUrl() {
        mExpectedException.expect(IllegalArgumentException.class);
        new RepositoryRestApi.Builder(null, "cookie").build();
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullCookie() {
        mExpectedException.expect(IllegalArgumentException.class);
        RepositoryRestApi restApi = new RepositoryRestApi.Builder(mWebMockRule.getRootUrl(), null).build();
    }

    @Test
    public void shouldThroughRestErrorOnSearchRequestIfHttpError() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(create500Response());

        restApiUnderTest.runReportExecution(ExecutionRequestOptions.newRequest("/any/uri"));
    }

    @Test
    public void bodyParameterShouldNotBeNullForRunReportExecution() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Body parameter value must not be null.");

        restApiUnderTest.runReportExecution(null);
    }

    @Test
    public void pathParameterShouldNotBeNullForRequestExecutionDetails() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Path parameter \"executionId\" value must not be null.");

        restApiUnderTest.requestReportExecutionDetails(null);
    }

    @Test
    public void pathParameterShouldNotBeNullForRequestExecutionStatus() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Path parameter \"executionId\" value must not be null.");

        restApiUnderTest.requestReportExecutionStatus(null);
    }

    @Test
    public void pathParameterShouldNotBeNullForCancelRequestExecution() {
        mExpectedException.expect(RestError.class);
        mExpectedException.expectMessage("Path parameter \"executionId\" value must not be null.");

        restApiUnderTest.cancelReportExecution(null);
    }

    @Test
    public void responseShouldNotBeCancelledIfResponseIs204() {
        mWebMockRule.enqueue(create204Response());

        boolean cancelled = restApiUnderTest.cancelReportExecution("any_id");

        assertThat(cancelled, is(false));
    }

    @Test
    public void responseShouldBeCancelledIfResponseIs200() {
        MockResponse response = create200Response();
        response.setBody(cancelledResponse.asString());
        mWebMockRule.enqueue(response);

        boolean cancelled = restApiUnderTest.cancelReportExecution("any_id");

        assertThat(cancelled, is(true));
    }

    private MockResponse create200Response() {
        return new MockResponse()
                .setStatus("HTTP/1.1 200 Ok");
    }

    private MockResponse create204Response() {
        return new MockResponse()
                .setStatus("HTTP/1.1 204 No Content");
    }

    private MockResponse create500Response() {
        return new MockResponse()
                .setStatus("HTTP/1.1 500 Internal Server Error");
    }
}
