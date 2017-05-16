package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecution;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionOptions;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardExportExecutionStatus;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
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

import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasBody.hasBody;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasMethod.wasMethod;
import static com.jaspersoft.android.sdk.test.matcher.IsRecordedRequestHasPath.hasPath;
import static com.jaspersoft.android.sdk.test.matcher.IsRecorderRequestContainsHeader.containsHeader;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardExportRestApiTest {

    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();
    private DashboardExportRestApi dashboardExportRestApi;

    @ResourceFile("json/dashboard_export_execution.json")
    TestResource dashboardExecutionCreate;
    @ResourceFile("json/dashboard_export_status_not_found.json")
    TestResource dashboardExportNotFound;
    @ResourceFile("json/dashboard_export_status_ready.json")
    TestResource dashboardExportReady;
    @ResourceFile("json/dashboard_export_result_not_found.json")
    TestResource dashboardExportResultNotFound;
    @ResourceFile("mediares/dashboard_export_result")
    TestResource dashboardExportResult;

    @Before
    public void setup() {
        TestResourceInjector.inject(this);
        Server server = Server.builder()
                .withBaseUrl(mWebMockRule.getRootUrl())
                .build();
        NetworkClient networkClient = server.newNetworkClient().build();
        dashboardExportRestApi = new DashboardExportRestApi(networkClient);
    }

    @Test
    public void shouldThroughRestErrorOnCreateExportRequestIfHttpError() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        dashboardExportRestApi.createExportJob(new DashboardExportExecutionOptions("/any/uri", null, null, "pdf", null));
    }

    @Test
    public void shouldThroughRestErrorOnExportStatusRequestIfHttpError() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        dashboardExportRestApi.getExportJobStatus("none");
    }

    @Test
    public void shouldThroughRestErrorOnExportResultRequestIfHttpError() throws Exception {
        mExpectedException.expect(HttpException.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        dashboardExportRestApi.getExportJobResult("none");
    }

    @Test
    public void bodyParameterShouldNotBeNullForCreateExportJob() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Dashboard export execution options should not be null");

        dashboardExportRestApi.createExportJob(null);
    }

    @Test
    public void jobIdParameterShouldNotBeNullForExportStatus() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Dashboard export job id should not be null");

        dashboardExportRestApi.getExportJobStatus(null);
    }

    @Test
    public void jobIdParameterShouldNotBeNullForExportResult() throws Exception {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Dashboard export job id should not be null");

        dashboardExportRestApi.getExportJobResult(null);
    }

    @Test
    public void jobExportShouldBeNotFound() throws Exception {
        MockResponse response = MockResponseFactory.create404().setBody(dashboardExportNotFound.asString());
        mWebMockRule.enqueue(response);

        mExpectedException.expect(HttpException.class);
        mExpectedException.expectMessage("Not Found 404");

        dashboardExportRestApi.getExportJobStatus("not existing");
    }

    @Test
    public void jobExportResultShouldBeNotFound() throws Exception {
        MockResponse response = MockResponseFactory.create404().setBody(dashboardExportResultNotFound.asString());
        mWebMockRule.enqueue(response);

        mExpectedException.expect(HttpException.class);
        mExpectedException.expectMessage("Not Found 404");

        dashboardExportRestApi.getExportJobResult("not existing");
    }

    @Test
    public void shouldCreateDashboardExport() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody(dashboardExecutionCreate.asString());
        mWebMockRule.enqueue(response);

        DashboardExportExecutionOptions options = new DashboardExportExecutionOptions("/my/uri", null, null, "pdf", null);
        DashboardExportExecution result = dashboardExportRestApi.createExportJob(options);

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/dashboardExecutions"));
        assertThat(request, wasMethod("POST"));
        assertThat(request, hasBody("{\"uri\":\"/my/uri\",\"format\":\"pdf\"}"));

        assertThat(result.getUri(), is("/public/Samples/Dashboards/1._Supermart_Dashboard"));
        assertThat(result.getFormat(), is("pdf"));
    }

    @Test
    public void shouldGetExportStatus() throws Exception {
        MockResponse response = MockResponseFactory.create200().setBody(dashboardExportReady.asString());
        mWebMockRule.enqueue(response);

        DashboardExportExecutionStatus result = dashboardExportRestApi.getExportJobStatus("job_id");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/dashboardExecutions/job_id/status"));
        assertThat(request, wasMethod("GET"));

        assertThat(result.getId(), is("013f367c-21a4-42f0-a796-bc117f94f3fc"));
        assertThat(result.getStatus(), is("ready"));
        assertThat(result.getProgress(), is(100));
    }

    @Test
    public void shouldGetExportResult() throws Exception {
        MockResponse response = MockResponseFactory.create200()
                .setHeader("Content-Type","image/png")
                .setBody(dashboardExportResult.asString());
        mWebMockRule.enqueue(response);

        OutputResource result = dashboardExportRestApi.getExportJobResult("job_id");

        RecordedRequest request = mWebMockRule.get().takeRequest();
        assertThat(request, containsHeader("Accept", "application/json; charset=UTF-8"));
        assertThat(request, hasPath("/rest_v2/dashboardExecutions/job_id/outputResource"));
        assertThat(request, wasMethod("GET"));

        assertThat(result.getMimeType(), is("image"));
    }
}
