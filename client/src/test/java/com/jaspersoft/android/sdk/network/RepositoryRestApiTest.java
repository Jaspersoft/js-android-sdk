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

import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.RestError;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
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
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryRestApiTest {

    @ResourceFile("json/all_resources.json")
    TestResource searchResponse;

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private RepositoryRestApi restApiUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestResourceInjector.inject(this);
        restApiUnderTest = new RepositoryRestApi.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void shouldReturnEmptyResponseForNoContentResponse() {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        ResourceSearchResult response = restApiUnderTest.searchResources("cookie", null);
        assertThat(response.getResources(), is(empty()));
    }

    @Test
    public void requestForSearchShouldParseHeaderResultCount() {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Result-Count", "100");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources("cookie", null);
        assertThat(response.getResultCount(), is(100));
    }

    @Test
    public void requestForSearchShouldParseHeaderTotalCount() {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Total-Count", "1000");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources("cookie", null);
        assertThat(response.getTotalCount(), is(1000));
    }

    @Test
    public void requestForSearchShouldParseHeaderStartIndex() {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Start-Index", "5");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources("cookie", null);
        assertThat(response.getStartIndex(), is(5));
    }

    @Test
    public void requestForSearchShouldParseHeaderNextOffset() {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Next-Offset", "10");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources("cookie", null);
        assertThat(response.getNextOffset(), is(10));
    }

    @Test
    public void searchResourcesShouldNotAcceptNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.searchResources(null, null);
    }

    @Test
    public void requestForReportResourceShouldNotAcceptNullUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");

        restApiUnderTest.requestReportResource("cookie", null);
    }

    @Test
    public void requestForReportResourceShouldNotAcceptNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.requestReportResource(null, "/uri");
    }

    @Test
    public void requestForFolderResourceShouldNotAcceptNullUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Folder uri should not be null");

        restApiUnderTest.requestFolderResource("cookie", null);
    }

    @Test
    public void requestForFolderResourceShouldNotAcceptNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");

        restApiUnderTest.requestFolderResource(null, "/my/uri");
    }

    @Test
    public void searchResourcesShouldThrowRestErrorOn500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.searchResources("cookie", null);
    }

    @Test
    public void requestReportResourceShouldThrowRestErrorOn500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestReportResource("cookie", "any_id");
    }

    @Test
    public void requestFolderResourceShouldThrowRestErrorOn500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestFolderResource("cookie", "any_id");
    }

    @Test
    public void searchEndpointShouldHandleMultipleResourceTypes() throws Exception {
        MockResponse response = MockResponseFactory.create200()
                .setBody("{\"resourceLookup\": []}");
        mWebMockRule.enqueue(response);

        Map<String, Object> params = new HashMap<>();
        params.put("folderUri", "/");

        Set<String> types = new HashSet<>();
        types.add("reportUnit");
        types.add("dashboard");
        params.put("type", types);

        restApiUnderTest.searchResources("cookie", params);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/resources?folderUri=/&type=reportUnit&type=dashboard"));
    }

    @Test
    public void shouldSearchResources() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);
        params.put("offset", 100);
        restApiUnderTest.searchResources("cookie", params);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/resources?limit=100&offset=100"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void shouldRequestReportResources() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        restApiUnderTest.requestReportResource("cookie", "/my/uri");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/resources/my/uri"));
        assertThat(request.getHeader("Accept"), is("application/repository.reportUnit+json"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void shouldRequestFolderResource() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        restApiUnderTest.requestFolderResource("cookie", "/my/uri");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/resources/my/uri"));
        assertThat(request.getHeader("Accept"), is("application/repository.folder+json"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }
}
