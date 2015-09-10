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

import com.jaspersoft.android.sdk.network.api.auth.Token;
import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.export.ExportInput;
import com.jaspersoft.android.sdk.network.entity.export.ExportResourceResponse;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
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

    @Mock
    Token<?> mToken;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestResourceInjector.inject(this);
        restApiUnderTest = new ReportExportRestApi.Builder()
                .setToken(mToken)
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void pathParameterShouldNotBeNullForRunRequestExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.runExportExecution(null, ExecutionRequestOptions.newInstance());
    }

    @Test
    public void bodyShouldNotBeNullForRunRequestExecution() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution options should not be null");

        restApiUnderTest.runExportExecution("any_id", null);
    }

    @Test
    public void executionIdShouldNotBeNullForCheckRequestExecutionStatus() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.checkExportExecutionStatus(null, "any_id");
    }

    @Test
    public void exportIdShouldNotBeNullForCheckRequestExecutionStatus() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Export id should not be null");

        restApiUnderTest.checkExportExecutionStatus("any_id", null);
    }

    @Test
    public void executionIdParameterShouldNotBeNullForAttachmentRequest() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Execution id should not be null");

        restApiUnderTest.requestExportAttachment(null, "any_id", "any_id");
    }

    @Test
    public void exportIdParameterShouldNotBeNullForAttachmentRequest() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Export id should not be null");

        restApiUnderTest.requestExportAttachment("any_id", null, "any_id");
    }

    @Test
    public void attachmentIdParameterShouldNotBeNullForAttachmentRequest() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Attachment id should not be null");

        restApiUnderTest.requestExportAttachment("any_id", "any_id", null);
    }

    @Test
    public void requestForOutputShouldParsePagesFromHeader() {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody("")
                .addHeader("report-pages", "1-10");
        mWebMockRule.enqueue(mockResponse);

        ExportResourceResponse resource = restApiUnderTest.requestExportOutput("any_id", "any_id");
        assertThat(resource.getPages(), is("1-10"));
    }

    @Test
    public void requestForOutputShouldParseIsFinalHeader() {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody("")
                .addHeader("output-final", "true");
        mWebMockRule.enqueue(mockResponse);

        ExportResourceResponse resource = restApiUnderTest.requestExportOutput("any_id", "any_id");
        assertThat(resource.isFinal(), is(true));
    }

    @Test
    public void requestForAttachmentShouldBeWrappedInsideInput() throws IOException {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(mResource.asString());
        mWebMockRule.enqueue(mockResponse);

        ExportInput resource = restApiUnderTest.requestExportAttachment("any_id", "any_id", "any_id");
        InputStream stream = resource.getStream();
        assertThat(stream, is(notNullValue()));
        stream.close();
    }

    @Test
    public void runExportExecutionShouldThrowRestErrorOn500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.runExportExecution("any_id", ExecutionRequestOptions.newInstance());
    }

    @Test
    public void checkExportExecutionStatusShouldThrowRestErrorOn500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.checkExportExecutionStatus("any_id", "any_id");
    }

    @Test
    public void requestExportOutputShouldThrowRestErrorOn500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestExportOutput("any_id", "any_id");
    }

    @Test
    public void requestExportAttachmentShouldThrowRestErrorOn500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestExportAttachment("any_id", "any_id", "any_id");
    }
}
