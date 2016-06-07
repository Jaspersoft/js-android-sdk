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

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasMethod.wasMethod;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasPath.hasPath;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasQuery.hasQuery;
import static com.jaspersoft.android.sdk.test.matcher.IsRecorderRequestContainsHeader.containsHeader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;

public class ReportScheduleRestApiTest {

    private final static String SEARCH_RESPONSE = "{ \"jobsummary\": [ { \"id\": 1898, \"version\": 0, \"reportUnitURI\": \"/adhoc/topics/AllAccounts\", \"label\": \"Sample Job Name\", \"owner\": \"jasperadmin|organization_1\", \"state\": { \"previousFireTime\": null, \"nextFireTime\": \"2013-10-12T00:00:00+03:00\", \"value\": \"NORMAL\" } }]}";

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();

    private ReportScheduleRestApi reportScheduleRestApi;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        Server server = Server.builder()
                .withBaseUrl(mWebMockRule.getRootUrl())
                .build();
        NetworkClient networkClient = server.newNetworkClient().build();
        reportScheduleRestApi = new ReportScheduleRestApi(networkClient);
    }

    @Test
    public void search_with_no_params() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody(SEARCH_RESPONSE));

        List<JobUnitEntity> entities = reportScheduleRestApi.searchJob(null);
        assertThat(entities, is(not(empty())));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat( request, hasPath("/rest_v2/jobs"));
        assertThat( request, wasMethod("GET"));
    }


    @Test
    public void search_should_return_empty_collection_if_no_content() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create204());

        List<JobUnitEntity> entities = reportScheduleRestApi.searchJob(null);
        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(entities, is(empty()));

        assertThat( request, hasPath("/rest_v2/jobs"));
        assertThat( request, wasMethod("GET"));
    }

    @Test
    public void search_fails_if_example_field_supplied() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Current version does not support 'example' search parameter");

        Map<String, Object> params = new HashMap<>();
        params.put("example", "{}");
        reportScheduleRestApi.searchJob(params);
    }

    @Test
    public void creates_post_job_request() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody("{}"));

        JobFormEntity form = new JobFormEntity();
        reportScheduleRestApi.createJob(form);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/job+json; charset=UTF-8"));
        assertThat(request, containsHeader("Content-Type", "application/job+json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/jobs"));
        assertThat(request, wasMethod("PUT"));
    }

    @Test
    public void updates_post_job_request() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody("{}"));

        JobFormEntity form = new JobFormEntity();
        reportScheduleRestApi.updateJob(1, form);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/job+json; charset=UTF-8"));
        assertThat(request, containsHeader("Content-Type", "application/job+json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/jobs/1"));
        assertThat(request, wasMethod("POST"));
    }

    @Test
    public void delete_jobs_operation_rejects_null_ids() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job ids should not be null");
        reportScheduleRestApi.deleteJobs(null);
    }

    @Test
    public void delete_jobs_operation_rejects_empty_ids() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Job ids should not be empty");
        reportScheduleRestApi.deleteJobs(Collections.<Integer>emptySet());
    }

    @Test
    public void creates_delete_jobs_request() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody("{\"jobId\":[5594,5645]}"));

        Set<Integer> deletedJobs = reportScheduleRestApi.deleteJobs(new HashSet<>(Arrays.asList(1, 2)));
        assertThat(deletedJobs, hasItems(5594, 5645));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasQuery("id", "1"));
        assertThat(request, hasQuery("id", "2"));
        assertThat(request, wasMethod("DELETE"));
    }

    @Test
    public void creates_get_jobs_request() throws Exception {
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody("{}"));

        reportScheduleRestApi.requestJob(1);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/job+json; charset=UTF-8"));
        assertThat(request, wasMethod("GET"));
        assertThat(request, hasPath("rest_v2/jobs/1"));
    }

}