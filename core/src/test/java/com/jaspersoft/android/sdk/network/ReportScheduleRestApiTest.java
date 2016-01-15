package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import retrofit.Retrofit;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.rules.ExpectedException.none;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters(method = "params")
    public void search_encode_query_param(String key, String value, String encodedQuery) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(key, value);

        List<JobUnit> result = reportScheduleRestApi.searchJob(params);
        assertThat(result, is(notNullValue()));

        RecordedRequest request = mWebMockRule.get().takeRequest();
        String path = request.getPath();

        String encodedPath = "/rest_v2/jobs?" + encodedQuery;
        assertThat("Should encode '" + key + "' with value '" + value + "' as '" + encodedQuery, path, is(encodedPath));
    }

    private Object[] params() {
        return $(
                $("reportUnitURI", "/some/report", "reportUnitURI=%2Fsome%2Freport"),
                $("owner", "jasperadmin|organization_1", "owner=jasperadmin%7Corganization_1"),
                $("label", "Sample Name", "label=Sample%20Name"),
                $("startIndex", "1", "startIndex=1"),
                $("numberOfRows", "-1", "numberOfRows=-1"),
                $("sortType", "NONE", "sortType=NONE"),
                $("isAscending", "true", "isAscending=true")
        );
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