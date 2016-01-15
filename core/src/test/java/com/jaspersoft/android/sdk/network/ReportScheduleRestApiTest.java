package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import retrofit.Retrofit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
        Retrofit retrofit = server.newRetrofit().build();
        reportScheduleRestApi = new ReportScheduleRestApi(retrofit);
        mWebMockRule.enqueue(MockResponseFactory.create200().setBody(SEARCH_RESPONSE));
    }

    @Test
    public void search_encodes_any_param() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportUnitURI", "/some/report");
        params.put("owner", "jasperadmin|organization_1");
        params.put("label", "Sample Name");
        params.put("startIndex", "1");
        params.put("numberOfRows", "-1");
        params.put("sortType", "NONE");
        params.put("isAscending", "true");

        List<JobUnit> result = reportScheduleRestApi.searchJob(params);
        assertThat(result, is(notNullValue()));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        String path = request.getPath();

        String encodedPath = "/rest_v2/jobs?owner=jasperadmin%7Corganization_1&startIndex=1&sortType=NONE&label=Sample%20Name&numberOfRows=-1&isAscending=true&reportUnitURI=%2Fsome%2Freport";
        assertThat("Should encode all params supplied in map", path, is(encodedPath));
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
}