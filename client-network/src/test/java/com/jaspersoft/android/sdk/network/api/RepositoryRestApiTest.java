/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;
import com.jaspersoft.android.sdk.test.resource.inject.TestResourceInjector;
import com.squareup.okhttp.mockwebserver.MockResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import rx.Observable;

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
        TestResourceInjector.inject(this);
        restApiUnderTest = new RepositoryRestApi.Builder(mWebMockRule.getRootUrl(), "cookie").build();
    }

    @Test
    public void shouldThroughRestErrorOnSearchRequestIfHttpError() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(create500Response());

        Observable<ResourceSearchResponse> call = restApiUnderTest.searchResources(null);
        call.toBlocking().first();
    }

    @Test
    public void shouldReturnEmptyResponseForNoContentResponse() {
        mWebMockRule.enqueue(create204Response());

        Observable<ResourceSearchResponse> call = restApiUnderTest.searchResources(null);
        ResourceSearchResponse response = call.toBlocking().first();

        assertThat(response.getResources(), is(empty()));
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
    public void requestForSearchShouldParseHeaderResultCount() {
        MockResponse mockResponse = create200Response()
                .setBody(searchResponse.asString())
                .addHeader("Result-Count", "100");
        mWebMockRule.enqueue(mockResponse);

        Observable<ResourceSearchResponse> call = restApiUnderTest.searchResources(null);
        ResourceSearchResponse response = call.toBlocking().first();
        assertThat(response.getResultCount(), is(100));
    }

    @Test
    public void requestForSearchShouldParseHeaderTotalCount() {
        MockResponse mockResponse = create200Response()
                .setBody(searchResponse.asString())
                .addHeader("Total-Count", "1000");
        mWebMockRule.enqueue(mockResponse);

        Observable<ResourceSearchResponse> call = restApiUnderTest.searchResources(null);
        ResourceSearchResponse response = call.toBlocking().first();
        assertThat(response.getTotalCount(), is(1000));
    }
    @Test
    public void requestForSearchShouldParseHeaderStartIndex() {
        MockResponse mockResponse = create200Response()
                .setBody(searchResponse.asString())
                .addHeader("Start-Index", "5");
        mWebMockRule.enqueue(mockResponse);

        Observable<ResourceSearchResponse> call = restApiUnderTest.searchResources(null);
        ResourceSearchResponse response = call.toBlocking().first();
        assertThat(response.getStartIndex(), is(5));
    }

    @Test
    public void requestForSearchShouldParseHeaderNextOffset() {
        MockResponse mockResponse = create200Response()
                .setBody(searchResponse.asString())
                .addHeader("Next-Offset", "10");
        mWebMockRule.enqueue(mockResponse);

        Observable<ResourceSearchResponse> call = restApiUnderTest.searchResources(null);
        ResourceSearchResponse response = call.toBlocking().first();
        assertThat(response.getNextOffset(), is(10));
    }

    @Test
    public void requestForReportResourceShouldNotAcceptNullUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");

        restApiUnderTest.requestReportResource(null);
    }

    @Test
    public void requestForDashboardResourceShouldNotAcceptNullUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Dashboard uri should not be null");

        restApiUnderTest.requestDashboardResource(null);
    }

    @Test
    public void requestForLegacyDashboardResourceShouldNotAcceptNullUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Legacy dashboard uri should not be null");

        restApiUnderTest.requestLegacyDashboardResource(null);
    }

    @Test
    public void requestForFolderResourceShouldNotAcceptNullUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Folder uri should not be null");

        restApiUnderTest.requestFolderResource(null);
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
