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

import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class ReportOptionRestApiTest {
    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private ReportOptionRestApi restApiUnderTest;

    @ResourceFile("json/report_option.json")
    TestResource reportOption;
    @ResourceFile("json/report_options_list.json")
    TestResource reportOptionsList;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        restApiUnderTest = new ReportOptionRestApi.Builder()
                .baseUrl(mWebMockRule.getRootUrl())
                .build();
    }

    @Test
    public void requestReportOptionsListShouldNotAllowNullReportUnitUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.requestReportOptionsList(null, "cookie");
    }

    @Test
    public void requestReportOptionsListShouldNotAllowNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");
        restApiUnderTest.requestReportOptionsList("/my/uri", null);
    }

    @Test
    public void createReportOptionShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.createReportOption(null, "label", Collections.EMPTY_MAP, false, "cookie");
    }

    @Test
    public void createReportOptionShouldNotAllowNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");
        restApiUnderTest.createReportOption("/my/uri", "label", Collections.EMPTY_MAP, false, null);
    }

    @Test
    public void createReportOptionShouldNotAllowNullOptionLabel() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option label should not be null");
        restApiUnderTest.createReportOption("any_id", null, Collections.EMPTY_MAP, false, "cookie");
    }

    @Test
    public void createReportOptionShouldNotAllowNullControlsValues() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Controls values should not be null");
        restApiUnderTest.createReportOption("any_id", "label", null, false, "cookie");
    }

    @Test
    public void updateReportOptionShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.updateReportOption(null, "option_id", Collections.EMPTY_MAP, "cookie");
    }

    @Test
    public void updateReportOptionShouldNotAllowNullOptionId() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option id should not be null");
        restApiUnderTest.updateReportOption("any_id", null, Collections.EMPTY_MAP, "cookie");
    }

    @Test
    public void updateReportOptionShouldNotAllowNullControlsValues() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Controls values should not be null");
        restApiUnderTest.updateReportOption("any_id", "option_id", null, "cookie");
    }

    @Test
    public void updateReportOptionShouldNotAllowNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");
        restApiUnderTest.updateReportOption("any_id", "option_id", Collections.EMPTY_MAP, null);
    }

    @Test
    public void deleteReportOptionShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.deleteReportOption(null, "option_id", "cookie");
    }

    @Test
    public void deleteReportOptionShouldNotAllowNullOptionId() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option id should not be null");
        restApiUnderTest.deleteReportOption("any_id", null, "cookie");
    }

    @Test
    public void deleteReportOptionShouldNotAllowNullToken() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Request token should not be null");
        restApiUnderTest.deleteReportOption("any_id", "option_id", null);
    }

    @Test
    public void apiShouldListReportOptions() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200().setBody(reportOptionsList.asString());
        mWebMockRule.enqueue(mockResponse);

        Set<ReportOption> response = restApiUnderTest.requestReportOptionsList("/any/uri", "cookie");
        assertThat(response, is(not(empty())));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void apiShouldCreateReportOption() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200().setBody(reportOption.asString());
        mWebMockRule.enqueue(mockResponse);

        Map<String, Set<String>> params = new HashMap<>();
        params.put("sales_fact_ALL__store_sales_2013_1", Collections.singleton("19"));

        ReportOption reportOption = restApiUnderTest.createReportOption("/any/uri", "my label", params, true, "cookie");
        assertThat(reportOption.getId(), is("my_label"));
        assertThat(reportOption.getLabel(), is("my label"));
        assertThat(reportOption.getUri(), is("/public/Samples/Reports/my_label"));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options?label=my%20label&overwrite=true"));
        assertThat(request.getBody().readUtf8(), is("{\"sales_fact_ALL__store_sales_2013_1\":[\"19\"]}"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void apiShouldUpdateReportOption() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        Map<String, Set<String>> params = new HashMap<>();
        params.put("sales_fact_ALL__store_sales_2013_1", Collections.singleton("22"));

        restApiUnderTest.updateReportOption("/any/uri", "option_id", params, "cookie");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options/option_id"));
        assertThat(request.getMethod(), is("PUT"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void apiShouldDeleteReportOption() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        restApiUnderTest.deleteReportOption("/any/uri", "option_id", "cookie");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options/option_id"));
        assertThat(request.getMethod(), is("DELETE"));
        assertThat(request.getHeader("Cookie"), is("cookie"));
    }

    @Test
    public void requestReportOptionsListShouldThrow500Error() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestReportOptionsList("any_id", "cookie");
    }

    @Test
    public void updateReportOptionShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.updateReportOption("any_id", "option_id", Collections.EMPTY_MAP, "cookie");
    }

    @Test
    public void deleteReportOptionShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.deleteReportOption("any_id", "option_id", "cookie");
    }
}
