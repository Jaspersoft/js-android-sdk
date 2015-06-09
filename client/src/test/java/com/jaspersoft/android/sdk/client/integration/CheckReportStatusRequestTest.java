package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.CheckReportStatusRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;
import com.jaspersoft.android.sdk.client.util.FactoryGirl;
import com.jaspersoft.android.sdk.client.util.ServerUnderTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.FakeHttp;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.robolectric.ParameterizedRobolectricTestRunner.*;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CheckReportStatusRequestTest {

    @Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"5.5", "http://mobiledemo.jaspersoft.com/jasperserver-pro", "XML"},
                {"5.5", "http://mobiledemo.jaspersoft.com/jasperserver-pro", "JSON"},
        });
    }

    private final ServerUnderTest mServer;
    private final String mDataType;

    public CheckReportStatusRequestTest(String versionCode, String url, String dataType) {
        mServer = ServerUnderTest.createBuilderWithDefaults()
                .setVersionCode(versionCode)
                .setServerUrl(url)
                .build();
        mDataType = dataType;
    }

    @Test
    public void requestShouldReportStatus() throws Exception {
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);

        FactoryGirl factoryGirl = FactoryGirl.newInstance();
        JsRestClient jsRestClient = factoryGirl.createJsRestClient(mDataType, mServer);
        ReportExecutionRequest reportExecutionRequest = factoryGirl.createExecutionData(jsRestClient);
        RunReportExecutionRequest runReportExecutionRequest = new RunReportExecutionRequest(jsRestClient, reportExecutionRequest);

        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        CheckReportStatusRequest checkReportStatusRequest = new CheckReportStatusRequest(jsRestClient, requestId);
        ReportStatusResponse response = checkReportStatusRequest.loadDataFromNetwork();
        assertThat(response.getStatus(), notNullValue());
    }

}
