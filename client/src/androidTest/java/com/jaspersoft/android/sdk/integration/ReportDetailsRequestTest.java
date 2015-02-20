package com.jaspersoft.android.sdk.integration;

import com.jaspersoft.android.sdk.client.async.request.ReportDetailsRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.integration.utils.ProtoInstrumentation;
import com.jaspersoft.android.sdk.integration.utils.SampleData;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportDetailsRequestTest extends ProtoInstrumentation {

    private RunReportExecutionRequest runReportExecutionRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ReportExecutionRequest reportExecutionRequest = SampleData.getSampleExecutionData(getJsRestClient(), RESOURCE_URI);
        runReportExecutionRequest = new RunReportExecutionRequest(getJsRestClient(), reportExecutionRequest);
    }

    public void test_requestReportDetails() throws Exception {
        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        ReportDetailsRequest reportDetailsRequest = new ReportDetailsRequest(getJsRestClient(), requestId);
        ReportExecutionResponse response = reportDetailsRequest.loadDataFromNetwork();
        assertFalse(response == null);
    }

}
