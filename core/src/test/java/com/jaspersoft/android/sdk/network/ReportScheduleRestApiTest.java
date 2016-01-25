package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasMethod.wasMethod;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasPath.hasPath;
import static com.jaspersoft.android.sdk.test.matcher.IsRecorderRequestContainsHeader.containsHeader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody(SEARCH_RESPONSE));
    }

    @Test
    public void search_with_no_params() throws Exception {
        reportScheduleRestApi.searchJob(null);
        RecordedRequest request = mWebMockRule.get().takeRequest();
        String path = request.getPath();
        assertThat("Should send no params", path, is("/rest_v2/jobs"));
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
}