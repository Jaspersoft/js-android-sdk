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

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.export.ExportOutputResource;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
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

import java.io.InputStream;

import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasMethod.wasMethod;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasPath.hasPath;
import static com.jaspersoft.android.sdk.test.matcher.IsRecorderRequestContainsHeader.containsHeader;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportExportRestApiTest {

    private static final String EXECUTION_ID = "f3a9805a-4089-4b53-b9e9-b54752f91586";
    private static final String EXPORT_ID = "195a65cb-1762-450a-be2b-1196a02bb625";

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private ReportExportRestApi restApiUnderTest;

    @ResourceFile("json/root_folder.json")
    TestResource mResource;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        Server server = Server.builder()
                .withBaseUrl(mWebMockRule.getRootUrl())
                .build();
        NetworkClient networkClient = server.newNetworkClient().build();
        restApiUnderTest = new ReportExportRestApi(networkClient);
    }

    @Test
    public void executionIdShouldNotBeNullForRunRequestExecution() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.runExportExecution(null, ExecutionRequestOptions.create());
    }

    @Test
    public void bodyShouldNotBeNullForRunRequestExecution() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution options should not be null");

        restApiUnderTest.runExportExecution("any_id", null);
    }

    @Test
    public void executionIdShouldNotBeNullForCheckRequestExecutionStatus() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.checkExportExecutionStatus(null, "any_id");
    }

    @Test
    public void exportIdShouldNotBeNullForCheckRequestExecutionStatus() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Export id should not be null");

        restApiUnderTest.checkExportExecutionStatus("any_id", null);
    }

    @Test
    public void executionIdParameterShouldNotBeNullForAttachmentRequest() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestExportAttachment(null, "any_id", "any_id");
    }

    @Test
    public void exportIdParameterShouldNotBeNullForAttachmentRequest() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Export id should not be null");

        restApiUnderTest.requestExportAttachment("any_id", null, "any_id");
    }

    @Test
    public void attachmentIdParameterShouldNotBeNullForAttachmentRequest() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Attachment id should not be null");

        restApiUnderTest.requestExportAttachment("any_id", "any_id", null);
    }

    @Test
    public void requestForOutputShouldParsePagesFromHeader() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody("")
                .addHeader("report-pages", "1-10");
        mWebMockRule.enqueue(mockResponse);

        ExportOutputResource resource = restApiUnderTest.requestExportOutput(EXECUTION_ID, EXPORT_ID);
        assertThat(resource.getPages(), is("1-10"));
    }

    @Test
    public void requestForOutputShouldParseIsFinalHeader() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody("")
                .addHeader("output-final", "true");
        mWebMockRule.enqueue(mockResponse);

        ExportOutputResource resource = restApiUnderTest.requestExportOutput(EXECUTION_ID, EXPORT_ID);
        assertThat(resource.isFinal(), is(true));
    }

    @Test
    public void shouldRequestExportOutput() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200();
        mWebMockRule.enqueue(mockResponse);
        restApiUnderTest.requestExportOutput(EXECUTION_ID, "html;pages=1");

        RecordedRequest request = mWebMockRule.get().takeRequest();

        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/reportExecutions/f3a9805a-4089-4b53-b9e9-b54752f91586/exports/html;pages=1/outputResource?suppressContentDisposition=true"));
        assertThat(request, wasMethod("GET"));
    }

    @Test
    public void requestForAttachmentShouldBeWrappedInsideInput() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(mResource.asString());
        mWebMockRule.enqueue(mockResponse);

        OutputResource resource = restApiUnderTest.requestExportAttachment("any_id", "any_id", "any_id");
        InputStream stream = resource.getStream();
        assertThat(stream, is(notNullValue()));
        stream.close();
    }

    @Test
    public void shouldRequestExportAttachment() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(mResource.asString());
        mWebMockRule.enqueue(mockResponse);

        restApiUnderTest.requestExportAttachment(EXECUTION_ID, "html;pages=1", "attachment_id");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/reportExecutions/f3a9805a-4089-4b53-b9e9-b54752f91586/exports/html;pages=1/attachments/attachment_id"));
        assertThat(request, wasMethod("GET"));
    }

    @Test
    public void shouldRunExportExecution() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200();
        mWebMockRule.enqueue(mockResponse);

        restApiUnderTest.runExportExecution(EXECUTION_ID, ExecutionRequestOptions.create());

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/reportExecutions/f3a9805a-4089-4b53-b9e9-b54752f91586/exports"));
        assertThat(request, wasMethod("POST"));
    }

    @Test
    public void shouldCheckExportExecutionStatus() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200();
        mWebMockRule.enqueue(mockResponse);

        restApiUnderTest.checkExportExecutionStatus(EXECUTION_ID, "html;pages=1");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/reportExecutions/f3a9805a-4089-4b53-b9e9-b54752f91586/exports/html;pages=1/status"));
        assertThat(request, wasMethod("GET"));
    }

    @Test
    public void runExportExecutionShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.runExportExecution("any_id", ExecutionRequestOptions.create());
    }

    @Test
    public void checkExportExecutionStatusShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.checkExportExecutionStatus("any_id", "any_id");
    }

    @Test
    public void requestExportOutputShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestExportOutput("any_id", "any_id");
    }

    @Test
    public void requestExportAttachmentShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestExportAttachment("any_id", "any_id", "any_id");
    }
}
