package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.ReportDetailsRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.util.RealHttpRule;

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
public class ReportDetailsRequestTest extends ParametrizedTest {
    @Rule
    public RealHttpRule realHttpRule = new RealHttpRule();

    @ParameterizedRobolectricTestRunner.Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return ParametrizedTest.data();
    }


    public ReportDetailsRequestTest(String versionCode, String url, String dataType) {
        super(versionCode, url, dataType);
    }

    @Test
    public void shouldRequestReportDetails() throws Exception {
        JsRestClient jsRestClient = getJsRestClient();
        ReportExecutionRequest reportExecutionRequest = getFactoryGirl().createExecutionData(jsRestClient);

        RunReportExecutionRequest runReportExecutionRequest = new RunReportExecutionRequest(jsRestClient, reportExecutionRequest);
        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        ReportDetailsRequest reportDetailsRequest = new ReportDetailsRequest(jsRestClient, requestId);
        ReportExecutionResponse response = reportDetailsRequest.loadDataFromNetwork();
        assertThat(response, notNullValue());
    }
}
