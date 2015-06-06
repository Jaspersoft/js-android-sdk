package com.jaspersoft.android.sdk.integration;

import android.test.AndroidTestCase;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.ReportDetailsRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.integration.utils.FactoryGirl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportDetailsRequestTest extends AndroidTestCase {
    private RunReportExecutionRequest runReportExecutionRequest;
    private JsRestClient jsRestClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FactoryGirl factoryGirl = FactoryGirl.newInstance();
        jsRestClient = factoryGirl.createJsRestClient();
        ReportExecutionRequest reportExecutionRequest = factoryGirl.createExecutionData(jsRestClient);
        runReportExecutionRequest = new RunReportExecutionRequest(jsRestClient, reportExecutionRequest);
    }

    public void test_requestReportDetails() throws Exception {
        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        ReportDetailsRequest reportDetailsRequest = new ReportDetailsRequest(jsRestClient, requestId);
        ReportExecutionResponse response = reportDetailsRequest.loadDataFromNetwork();
        assertThat(response, notNullValue());
    }
}
