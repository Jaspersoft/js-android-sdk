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

package com.jaspersoft.android.sdk.network.api;

import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
import com.squareup.okhttp.mockwebserver.MockResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExecutionRestApiTest {

    private static final Map<String, String> SEARCH_PARAMS = new HashMap<String, String>();
    static {
        SEARCH_PARAMS.put("key", "value");
    }

    @ResourceFile("json/cancelled_report_response.json")
    TestResource cancelledResponse;
    @ResourceFile("json/search_execution_response.json")
    TestResource searchExecutionResponse;

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();
    private ReportExecutionRestApi restApiUnderTest;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        restApiUnderTest = new ReportExecutionRestApi.Builder()
                .tokenProvider(FakeTokenProvider.get())
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void shouldThroughRestErrorOnSearchRequestIfHttpError() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(create500Response());

        restApiUnderTest.runReportExecution(ReportExecutionRequestOptions.newRequest("/any/uri"));
    }

    @Test
    public void bodyParameterShouldNotBeNullForRunReportExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution options should not be null");

        restApiUnderTest.runReportExecution(null);
    }

    @Test
    public void pathParameterShouldNotBeNullForRequestExecutionDetails() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestReportExecutionDetails(null);
    }

    @Test
    public void pathParameterShouldNotBeNullForRequestExecutionStatus() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestReportExecutionStatus(null);
    }

    @Test
    public void pathParameterShouldNotBeNullForCancelRequestExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

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

    @Test
    public void executionSearchResponseShouldBeEmptyIfResponseIs204() throws IOException {
        mWebMockRule.enqueue(create204Response());

        ReportExecutionSearchResponse response = restApiUnderTest.searchReportExecution(SEARCH_PARAMS);
        assertThat(response.getItems(), is(empty()));
    }

    @Test
    public void executionSearchResponseShouldNotBeEmptyIfResponseIs200() throws IOException {
        MockResponse mockResponse = create200Response();
        mockResponse.setBody(searchExecutionResponse.asString());
        mWebMockRule.enqueue(mockResponse);

        ReportExecutionSearchResponse response = restApiUnderTest.searchReportExecution(SEARCH_PARAMS);
        assertThat(response.getItems(), is(not(empty())));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void executionUpdateRequestShouldBeSuccessIfResponseIs204() {
        mWebMockRule.enqueue(create204Response());

        boolean response = restApiUnderTest.updateReportExecution("any_id", Collections.EMPTY_MAP                                                                                       );
        assertThat(response, is(true));
    }

    @Test
    public void bodyParameterShouldNotBeNullForExecutionUpdate() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution params id should not be null");

        restApiUnderTest.updateReportExecution("any_id", null);
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
