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

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasMethod.wasMethod;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasPath.hasPath;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasQuery.hasQuery;
import static com.jaspersoft.android.sdk.test.matcher.IsRecorderRequestContainsHeader.containsHeader;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@RunWith(JUnitParamsRunner.class)
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
        Server server = Server.builder()
                .withBaseUrl(mWebMockRule.getRootUrl())
                .build();
        NetworkClient networkClient = server.newNetworkClient().build();
        restApiUnderTest = new RepositoryRestApi(networkClient);
    }

    @Test
    public void shouldReturnEmptyResponseForNoContentResponse() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        ResourceSearchResult response = restApiUnderTest.searchResources(null);
        assertThat(response.getResources(), is(empty()));
    }

    @Test
    public void requestForSearchShouldParseHeaderResultCount() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Result-Count", "100");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources(null);
        assertThat(response.getResultCount(), is(100));
    }

    @Test
    public void requestForSearchShouldParseHeaderTotalCount() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Total-Count", "1000");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources(null);
        assertThat(response.getTotalCount(), is(1000));
    }

    @Test
    public void requestForSearchShouldParseHeaderStartIndex() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Start-Index", "5");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources(null);
        assertThat(response.getStartIndex(), is(5));
    }

    @Test
    public void requestForSearchShouldParseHeaderNextOffset() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200()
                .setBody(searchResponse.asString())
                .addHeader("Next-Offset", "10");
        mWebMockRule.enqueue(mockResponse);

        ResourceSearchResult response = restApiUnderTest.searchResources(null);
        assertThat(response.getNextOffset(), is(10));
    }

    @Test
    public void requestForResourceByTypeShouldNotAcceptNullType() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report type should not be null");
        restApiUnderTest.requestResource("/", null);
    }

    @Test
    public void requestForResourceByTypeShouldNotAcceptNullUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.requestResource(null, "file");
    }

    @Test
    public void requestForFileContentShouldNotAcceptNullUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Resource uri should not be null");

        restApiUnderTest.requestResourceOutput(null);
    }

    @Test
    public void requestForRequestDashboardComponentsdNotAcceptNullUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Dashboard uri should not be null");

        restApiUnderTest.requestDashboardComponents(null);
    }

    @Test
    public void searchResourcesShouldThrowRestErrorOn500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.searchResources(null);
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

        restApiUnderTest.searchResources(params);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasQuery("folderUri", "/"));
        assertThat(request, hasQuery("type", "reportUnit"));
        assertThat(request, hasQuery("type", "dashboard"));
        assertThat(request, wasMethod("GET"));
    }

    @Test
    public void shouldSearchResources() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("limit", 100);
        params.put("offset", 100);
        restApiUnderTest.searchResources(params);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/resources?limit=100&offset=100"));
        assertThat(request, wasMethod("GET"));
    }

    @Test
    public void shouldRequestDashboardComponents() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        restApiUnderTest.requestDashboardComponents("/my/uri");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/dashboardComponentsSchema+json"));
        assertThat(request, hasPath("/rest_v2/resources/my/uri_files/components?expanded=false"));
        assertThat(request, wasMethod("GET"));
    }

    @Test
    public void shouldRequestFileContent() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        restApiUnderTest.requestResourceOutput("/my/uri");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, hasPath("/rest_v2/resources/my/uri"));
        assertThat(request, wasMethod("GET"));
    }

    @Test
    @Parameters({
            "reportUnit|com.jaspersoft.android.sdk.network.entity.resource.ReportLookup",
            "folder|com.jaspersoft.android.sdk.network.entity.resource.FolderLookup",
            "file|com.jaspersoft.android.sdk.network.entity.resource.FileLookup"
    })
    public void shouldRequestReportResources(String type, String className) throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody("{}"));

        ResourceLookup expected = restApiUnderTest.requestResource("/my/uri", type);
        assertThat(expected, instanceOf(Class.forName(className)));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", String.format("application/repository.%s+json", type)));
        assertThat(request, hasPath("/rest_v2/resources/my/uri"));
        assertThat(request, wasMethod("GET"));
    }

}
