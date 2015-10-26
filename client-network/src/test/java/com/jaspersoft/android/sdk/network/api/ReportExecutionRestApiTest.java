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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExecutionRestApiTest {

    private static final Map<String, String> SEARCH_PARAMS = new HashMap<String, String>();
    private static final List<Map<String, Set<String>>> PARAMS = new ArrayList<>();
    static {
        Map<String, Set<String>> reportParameter = new HashMap<>();
        reportParameter.put("key", new HashSet<String>(Collections.singletonList("value")));
        PARAMS.add(reportParameter);
    }

    static {
        SEARCH_PARAMS.put("key", "value");
    }

    @ResourceFile("json/cancelled_report_response.json")
    TestResource cancelledResponse;
    @ResourceFile("json/search_execution_response.json")
    TestResource searchExecutionResponse;
    @ResourceFile("json/report_execution_response.json")
    TestResource reportExecutionResponse;
    @ResourceFile("json/report_execution_details.json")
    TestResource reportExecutionDetailsResponse;

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();
    private ReportExecutionRestApi restApiUnderTest;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        restApiUnderTest = new ReportExecutionRestApi.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void shouldThroughRestErrorOnSearchRequestIfHttpError() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.runReportExecution("cookie", ReportExecutionRequestOptions.newRequest("/any/uri"));
    }

    @Test
    public void bodyParameterShouldNotBeNullForRunReportExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution options should not be null");

        restApiUnderTest.runReportExecution("cookie", null);
    }

    @Test
    public void tokenShouldNotBeNullForRunReportExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest("/uri");
        restApiUnderTest.runReportExecution(null, options);
    }

    @Test
    public void executionIdShouldNotBeNullForRequestExecutionDetails() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestReportExecutionDetails("cookie", null);
    }

    @Test
    public void tokenShouldNotBeNullForRequestExecutionDetails() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.requestReportExecutionDetails(null, "exec_id");
    }

    @Test
    public void executionIdShouldNotBeNullForRequestExecutionStatus() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestReportExecutionStatus("cookie", null);
    }

    @Test
    public void tokenShouldNotBeNullForRequestExecutionStatus() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.requestReportExecutionStatus(null, "exec_id");
    }

    @Test
    public void executionIdShouldNotBeNullForCancelRequestExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.cancelReportExecution("cookie", null);
    }

    @Test
    public void tokenShouldNotBeNullForCancelRequestExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.cancelReportExecution(null, "exec_id");
    }

    @Test
    public void bodyParameterShouldNotBeNullForExecutionUpdate() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution params should not be null");

        restApiUnderTest.updateReportExecution("cookie", "any_id", null);
    }

    @Test
    public void bodyParameterShouldNotBeEmptyForExecutionUpdate() {
        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Execution params should not be empty");

        restApiUnderTest.updateReportExecution("cookie", "any_id", Collections.<Map<String, Set<String>>>emptyList());
    }

    @Test
    public void shouldStartReportExecution() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody(reportExecutionResponse.asString());
        mWebMockRule.enqueue(response);

        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest("/my/uri");
        restApiUnderTest.runReportExecution("cookie", options);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions"));
        assertThat(request.getBody().readUtf8(), is("{\"reportUnitUri\":\"/my/uri\"}"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
        assertThat(request.getMethod(), is("POST"));
    }

    @Test
    public void shouldRequestReportExecutionDetails() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody(reportExecutionDetailsResponse.asString());
        mWebMockRule.enqueue(response);

        ReportExecutionDescriptor details = restApiUnderTest.requestReportExecutionDetails("cookie", "exec_id");
        assertThat(details, is(notNullValue()));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
        assertThat(request.getMethod(), is("GET"));
    }

    @Test
    public void shouldRequestReportExecutionStatus() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody("{\"value\":\"execution\"}");
        mWebMockRule.enqueue(response);

        ExecutionStatus status = restApiUnderTest.requestReportExecutionStatus("cookie", "exec_id");
        assertThat(status, is(notNullValue()));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id/status"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
        assertThat(request.getMethod(), is("GET"));
    }

    @Test
    public void shouldCancelReportExecution() throws Exception {
        MockResponse response = MockResponseFactory.create200();
        mWebMockRule.enqueue(response);

        restApiUnderTest.cancelReportExecution("cookie", "exec_id");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id/status"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
        assertThat(request.getBody().readUtf8(), is("{\"value\":\"cancelled\"}"));
        assertThat(request.getMethod(), is("PUT"));
    }

    @Test
    public void shouldUpdateReportExecution() throws Exception {
        MockResponse response = MockResponseFactory.create204();
        mWebMockRule.enqueue(response);

        restApiUnderTest.updateReportExecution("cookie", "exec_id", PARAMS);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id/parameters"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
        assertThat(request.getBody().readUtf8(), is("[{\"key\":[\"value\"]}]"));
        assertThat(request.getMethod(), is("POST"));
    }

    @Test
    public void responseShouldNotBeCancelledIfResponseIs204() {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        boolean cancelled = restApiUnderTest.cancelReportExecution("cookie", "any_id");

        assertThat(cancelled, is(false));
    }

    @Test
    public void responseShouldBeCancelledIfResponseIs200() {
        MockResponse response = MockResponseFactory.create200().setBody(cancelledResponse.asString());
        mWebMockRule.enqueue(response);

        boolean cancelled = restApiUnderTest.cancelReportExecution("cookie", "any_id");

        assertThat(cancelled, is(true));
    }

    @Test
    public void executionSearchResponseShouldBeEmptyIfResponseIs204() throws IOException {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        ReportExecutionSearchResponse response = restApiUnderTest.searchReportExecution("cookie", SEARCH_PARAMS);
        assertThat(response.getItems(), is(empty()));
    }

    @Test
    public void executionSearchResponseShouldNotBeEmptyIfResponseIs200() throws IOException {
        MockResponse mockResponse = MockResponseFactory.create200();
        mockResponse.setBody(searchExecutionResponse.asString());
        mWebMockRule.enqueue(mockResponse);

        ReportExecutionSearchResponse response = restApiUnderTest.searchReportExecution("cookie", SEARCH_PARAMS);
        assertThat(response.getItems(), is(not(empty())));
    }

    @Test
    public void executionUpdateRequestShouldBeSuccessIfResponseIs204() {
        mWebMockRule.enqueue(MockResponseFactory.create204());
        boolean response = restApiUnderTest.updateReportExecution("cookie", "any_id", PARAMS);
        assertThat(response, is(true));
    }
}
