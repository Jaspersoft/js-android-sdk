package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.CheckReportStatusRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;
import com.jaspersoft.android.sdk.util.StaticCacheHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.FakeHttp;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CheckReportStatusRequestTest extends ParametrizedTest {

    @ParameterizedRobolectricTestRunner.Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return ParametrizedTest.data();
    }

    public CheckReportStatusRequestTest(String versionCode, String url, String dataType) {
        super(versionCode, url, dataType);
    }

    @Test
    public void requestShouldReportStatus() throws Exception {
        StaticCacheHelper.clearCache();
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);

        JsRestClient jsRestClient = getJsRestClient();
        ReportExecutionRequest reportExecutionRequest = getFactoryGirl().createExecutionData(jsRestClient);
        RunReportExecutionRequest runReportExecutionRequest = new RunReportExecutionRequest(jsRestClient, reportExecutionRequest);

        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        CheckReportStatusRequest checkReportStatusRequest = new CheckReportStatusRequest(jsRestClient, requestId);
        ReportStatusResponse response = checkReportStatusRequest.loadDataFromNetwork();
        assertThat(response.getStatus(), notNullValue());
    }

}
