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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionSearchResponse;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
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
import retrofit.Retrofit;

import java.util.*;

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
    private static final List<ReportParameter> PARAMS = Collections.singletonList(
            new ReportParameter("key", new HashSet<String>(Collections.singletonList("value")))
    );

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restApiUnderTest = new ReportExecutionRestApiImpl(retrofit);
    }

    @Test
    public void shouldThroughRestErrorOnSearchRequestIfHttpError() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.runReportExecution(ReportExecutionRequestOptions.newRequest("/any/uri"));
    }

    @Test
    public void bodyParameterShouldNotBeNullForRunReportExecution() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution options should not be null");

        restApiUnderTest.runReportExecution(null);
    }

    @Test
    public void executionIdShouldNotBeNullForRequestExecutionDetails() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestReportExecutionDetails(null);
    }

    @Test
    public void executionIdShouldNotBeNullForRequestExecutionStatus() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestReportExecutionStatus(null);
    }

    @Test
    public void executionIdShouldNotBeNullForCancelRequestExecution() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.cancelReportExecution(null);
    }

    @Test
    public void bodyParameterShouldNotBeNullForExecutionUpdate() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution params should not be null");

        restApiUnderTest.updateReportExecution("any_id", null);
    }

    @Test
    public void bodyParameterShouldNotBeEmptyForExecutionUpdate() throws Exception {
        mExpectedException.expect(IllegalArgumentException.class);
        mExpectedException.expectMessage("Execution params should not be empty");

        restApiUnderTest.updateReportExecution("any_id", Collections.<ReportParameter>emptyList());
    }

    @Test
    public void shouldStartReportExecution() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody(reportExecutionResponse.asString());
        mWebMockRule.enqueue(response);

        ReportExecutionRequestOptions options = ReportExecutionRequestOptions.newRequest("/my/uri");
        restApiUnderTest.runReportExecution(options);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions"));
        assertThat(request.getBody().readUtf8(), is("{\"reportUnitUri\":\"/my/uri\"}"));
        assertThat(request.getHeader("Cookie"), is("key=value"));
        assertThat(request.getMethod(), is("POST"));
    }

    @Test
    public void shouldRequestReportExecutionDetails() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody(reportExecutionDetailsResponse.asString());
        mWebMockRule.enqueue(response);

        ReportExecutionDescriptor details = restApiUnderTest.requestReportExecutionDetails("exec_id");
        assertThat(details, is(notNullValue()));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id"));
        assertThat(request.getHeader("Cookie"), is("key=value"));
        assertThat(request.getMethod(), is("GET"));
    }

    @Test
    public void shouldRequestReportExecutionStatus() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody("{\"value\":\"execution\"}");
        mWebMockRule.enqueue(response);

        ExecutionStatus status = restApiUnderTest.requestReportExecutionStatus("exec_id");
        assertThat(status, is(notNullValue()));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id/status"));
        assertThat(request.getHeader("Cookie"), is("key=value"));
        assertThat(request.getMethod(), is("GET"));
    }

    @Test
    public void shouldCancelReportExecution() throws Exception {
        MockResponse response = MockResponseFactory.create200();
        mWebMockRule.enqueue(response);

        restApiUnderTest.cancelReportExecution("exec_id");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id/status"));
        assertThat(request.getHeader("Cookie"), is("key=value"));
        assertThat(request.getBody().readUtf8(), is("{\"value\":\"cancelled\"}"));
        assertThat(request.getMethod(), is("PUT"));
    }

    @Test
    public void shouldUpdateReportExecution() throws Exception {
        MockResponse response = MockResponseFactory.create204();
        mWebMockRule.enqueue(response);

        restApiUnderTest.updateReportExecution("exec_id", PARAMS);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/exec_id/parameters"));
        assertThat(request.getHeader("Cookie"), is("key=value"));
        assertThat(request.getBody().readUtf8(), is("[{\"name\":\"key\",\"value\":[\"value\"]}]"));
        assertThat(request.getMethod(), is("POST"));
    }

    @Test
    public void responseShouldNotBeCancelledIfResponseIs204() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        boolean cancelled = restApiUnderTest.cancelReportExecution("any_id");

        assertThat(cancelled, is(false));
    }

    @Test
    public void responseShouldBeCancelledIfResponseIs200() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody(cancelledResponse.asString());
        mWebMockRule.enqueue(response);

        boolean cancelled = restApiUnderTest.cancelReportExecution("any_id");

        assertThat(cancelled, is(true));
    }

    @Test
    public void executionSearchResponseShouldBeEmptyIfResponseIs204() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        ReportExecutionSearchResponse response = restApiUnderTest.searchReportExecution(SEARCH_PARAMS);
        assertThat(response.getItems(), is(empty()));
    }

    @Test
    public void executionSearchResponseShouldNotBeEmptyIfResponseIs200() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200();
        mockResponse.setBody(searchExecutionResponse.asString());
        mWebMockRule.enqueue(mockResponse);

        ReportExecutionSearchResponse response = restApiUnderTest.searchReportExecution(SEARCH_PARAMS);
        assertThat(response.getItems(), is(not(empty())));
    }

    @Test
    public void executionUpdateRequestShouldBeSuccessIfResponseIs204() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create204());
        boolean response = restApiUnderTest.updateReportExecution("any_id", PARAMS);
        assertThat(response, is(true));
    }
}
