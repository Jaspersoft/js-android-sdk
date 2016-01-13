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

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
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
import retrofit.Retrofit;

import java.util.*;

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

    private final static List<ReportParameter> REPORT_PARAMS  =
            Collections.singletonList(
                    new ReportParameter("sales_fact_ALL__store_sales_2013_1", Collections.singleton("19"))
            );

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
        Server server = Server.builder()
                .withBaseUrl(mWebMockRule.getRootUrl())
                .build();
        Retrofit retrofit = server.newRetrofit().build();
        restApiUnderTest = new ReportOptionRestApiImpl(retrofit);
    }

    @Test
    public void requestReportOptionsListShouldNotAllowNullReportUnitUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.requestReportOptionsList(null);
    }

    @Test
    public void createReportOptionShouldNotAllowNullReportUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.createReportOption(null, "label", REPORT_PARAMS, false);
    }

    @Test
    public void createReportOptionShouldNotAllowNullOptionLabel() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option label should not be null");
        restApiUnderTest.createReportOption("any_id", null, REPORT_PARAMS, false);
    }

    @Test
    public void createReportOptionShouldNotAllowNullControlsValues() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Parameters values should not be null");
        restApiUnderTest.createReportOption("any_id", "label", null, false);
    }

    @Test
    public void updateReportOptionShouldNotAllowNullReportUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.updateReportOption(null, "option_id", REPORT_PARAMS);
    }

    @Test
    public void updateReportOptionShouldNotAllowNullOptionId() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option id should not be null");
        restApiUnderTest.updateReportOption("any_id", null, REPORT_PARAMS);
    }

    @Test
    public void updateReportOptionShouldNotAllowNullControlsValues() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Parameters values should not be null");
        restApiUnderTest.updateReportOption("any_id", "option_id", null);
    }

    @Test
    public void deleteReportOptionShouldNotAllowNullReportUri() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.deleteReportOption(null, "option_id");
    }

    @Test
    public void deleteReportOptionShouldNotAllowNullOptionId() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option id should not be null");
        restApiUnderTest.deleteReportOption("any_id", null);
    }

    @Test
    public void apiShouldListReportOptions() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200().setBody(reportOptionsList.asString());
        mWebMockRule.enqueue(mockResponse);

        Set<ReportOption> response = restApiUnderTest.requestReportOptionsList("/any/uri");
        assertThat(response, is(not(empty())));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options"));
    }

    @Test
    public void apiShouldCreateReportOption() throws Exception {
        MockResponse mockResponse = MockResponseFactory.create200().setBody(reportOption.asString());
        mWebMockRule.enqueue(mockResponse);

        ReportOption reportOption = restApiUnderTest.createReportOption("/any/uri", "my label", REPORT_PARAMS, true);
        assertThat(reportOption.getId(), is("my_label"));
        assertThat(reportOption.getLabel(), is("my label"));
        assertThat(reportOption.getUri(), is("/public/Samples/Reports/my_label"));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options?label=my%20label&overwrite=true"));
        assertThat(request.getBody().readUtf8(), is("{\"sales_fact_ALL__store_sales_2013_1\":[\"19\"]}"));
    }

    @Test
    public void apiShouldUpdateReportOption() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        Map<String, Set<String>> params = new HashMap<>();
        params.put("sales_fact_ALL__store_sales_2013_1", Collections.singleton("22"));

        restApiUnderTest.updateReportOption("/any/uri", "option_id", REPORT_PARAMS);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options/option_id"));
        assertThat(request.getMethod(), is("PUT"));
    }

    @Test
    public void apiShouldDeleteReportOption() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200());

        restApiUnderTest.deleteReportOption("/any/uri", "option_id");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request.getPath(), is("/rest_v2/reports/any/uri/options/option_id"));
        assertThat(request.getMethod(), is("DELETE"));
    }

    @Test
    public void requestReportOptionsListShouldThrow500Error() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestReportOptionsList("any_id");
    }

    @Test
    public void updateReportOptionShouldThrowRestErrorFor500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.updateReportOption("any_id", "option_id", REPORT_PARAMS);
    }

    @Test
    public void deleteReportOptionShouldThrowRestErrorFor500() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.deleteReportOption("any_id", "option_id");
    }
}
