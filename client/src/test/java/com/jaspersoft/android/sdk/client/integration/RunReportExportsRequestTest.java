package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExportOutputRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExportsRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ExportExecution;
import com.jaspersoft.android.sdk.client.oxm.report.ExportsRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportDataResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.util.RealHttpRule;
import com.jaspersoft.android.sdk.client.util.TargetDataType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@TargetDataType(values = {"XML", "JSON"})
public class RunReportExportsRequestTest extends ParametrizedTest {
    @Rule
    public RealHttpRule realHttpRule = new RealHttpRule();

    @ParameterizedRobolectricTestRunner.Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return ParametrizedTest.data(RunReportExportsRequestTest.class);
    }

    public RunReportExportsRequestTest(String versionCode, String url, String dataType) {
        super(versionCode, url, dataType);
    }

    @Test
    public void shouldRequestForExportsOnReport() throws Exception {
        JsRestClient jsRestClient = getJsRestClient();
        ReportExecutionRequest reportExecutionRequest = getFactoryGirl().createExecutionData(jsRestClient);
        RunReportExecutionRequest runReportExecutionRequest = new RunReportExecutionRequest(jsRestClient, reportExecutionRequest);

        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        ExportsRequest exr = new ExportsRequest();
        exr.setMarkupType(ReportExecutionRequest.MARKUP_TYPE_EMBEDDABLE);
        exr.setAllowInlineScripts(false);
        exr.setOutputFormat("html");
        exr.setPages("1");

        RunReportExportsRequest runReportExportsRequest = new RunReportExportsRequest(jsRestClient, exr, requestId);
        ExportExecution runReportExportsResponse = runReportExportsRequest.loadDataFromNetwork();
        assertThat(runReportExportsResponse.getStatus(), notNullValue());
        assertThat(runReportExportsResponse.getId(), notNullValue());

        String executionId = runReportExportsResponse.getId();
        RunReportExportOutputRequest runReportExportOutputRequest
                = new RunReportExportOutputRequest(jsRestClient, requestId, executionId);
        ReportDataResponse response = runReportExportOutputRequest.loadDataFromNetwork();
        assertThat(response.getData(), notNullValue());
    }
}
