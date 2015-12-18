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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
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

    @ResourceFile("json/root_folder.json")
    TestResource mResource;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        restApiUnderTest = new ReportExportRestApi.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void executionIdShouldNotBeNullForRunRequestExecution() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.runExportExecution("cookie", null, ExecutionRequestOptions.create());
    }

    @Test
    public void bodyShouldNotBeNullForRunRequestExecution() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution options should not be null");

        restApiUnderTest.runExportExecution("cookie", "any_id", null);
    }

    @Test
    public void tokenShouldNotBeNullForRunRequestExecution() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.runExportExecution(null, "any_id", ExecutionRequestOptions.create());
    }

    @Test
    public void executionIdShouldNotBeNullForCheckRequestExecutionStatus() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.checkExportExecutionStatus("cookie", null, "any_id");
    }

    @Test
    public void exportIdShouldNotBeNullForCheckRequestExecutionStatus() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Export id should not be null");

        restApiUnderTest.checkExportExecutionStatus("cookie", "any_id", null);
    }

    @Test
    public void tokenShouldNotBeNullForCheckRequestExecutionStatus() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.checkExportExecutionStatus(null, "any_id", "any_id");
    }

    @Test
    public void executionIdParameterShouldNotBeNullForAttachmentRequest() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestExportAttachment("cookie", null, "any_id", "any_id");
    }

    @Test
    public void exportIdParameterShouldNotBeNullForAttachmentRequest() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Export id should not be null");

        restApiUnderTest.requestExportAttachment("cookie", "any_id", null, "any_id");
    }

    @Test
    public void attachmentIdParameterShouldNotBeNullForAttachmentRequest() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Attachment id should not be null");

        restApiUnderTest.requestExportAttachment("cookie", "any_id", "any_id", null);
    }

    @Test
    public void tokenIdParameterShouldNotBeNullForAttachmentRequest() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.requestExportAttachment(null, "any_id", "any_id", "any_id");
    }

    @Test
    public void requestForOutputShouldParsePagesFromHeader() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody("")
                .addHeader("report-pages", "1-10");
        mWebMockRule.enqueue(mockResponse);

        ExportOutputResource resource = restApiUnderTest.requestExportOutput("cookie", "any_id", "any_id");
        assertThat(resource.getPages(), is("1-10"));
    }

    @Test
    public void requestForOutputShouldParseIsFinalHeader() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody("")
                .addHeader("output-final", "true");
        mWebMockRule.enqueue(mockResponse);

        ExportOutputResource resource = restApiUnderTest.requestExportOutput("cookie", "execution_id", "export_id");
        assertThat(resource.isFinal(), is(true));
    }

    @Test
    public void shouldRequestExportOutput() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200();
        mWebMockRule.enqueue(mockResponse);
        restApiUnderTest.requestExportOutput("cookie", "execution_id", "html;pages=1");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/execution_id/exports/html;pages=1/outputResource?suppressContentDisposition=true"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void requestForAttachmentShouldBeWrappedInsideInput() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(mResource.asString());
        mWebMockRule.enqueue(mockResponse);

        OutputResource resource = restApiUnderTest.requestExportAttachment("cookie", "any_id", "any_id", "any_id");
        InputStream stream = resource.getStream();
        assertThat(stream, is(notNullValue()));
        stream.close();
    }

    @Test
    public void shouldRequestExportAttachment() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(mResource.asString());
        mWebMockRule.enqueue(mockResponse);

        restApiUnderTest.requestExportAttachment("cookie", "execution_id", "html;pages=1", "attachment_id");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/execution_id/exports/html;pages=1/attachments/attachment_id"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void shouldRunExportExecution() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200();
        mWebMockRule.enqueue(mockResponse);

        restApiUnderTest.runExportExecution("cookie", "execution_id", ExecutionRequestOptions.create());

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/execution_id/exports"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void shouldCheckExportExecutionStatus() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200();
        mWebMockRule.enqueue(mockResponse);

        restApiUnderTest.checkExportExecutionStatus("cookie", "execution_id", "html;pages=1");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reportExecutions/execution_id/exports/html;pages=1/status"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void runExportExecutionShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.runExportExecution("cookie", "any_id", ExecutionRequestOptions.create());
    }

    @Test
    public void checkExportExecutionStatusShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.checkExportExecutionStatus("cookie", "any_id", "any_id");
    }

    @Test
    public void requestExportOutputShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestExportOutput("cookie", "any_id", "any_id");
    }

    @Test
    public void requestExportAttachmentShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestExportAttachment("cookie", "any_id", "any_id", "any_id");
    }
}
