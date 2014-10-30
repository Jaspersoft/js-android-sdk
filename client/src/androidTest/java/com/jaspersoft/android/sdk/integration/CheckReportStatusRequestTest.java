package com.jaspersoft.android.sdk.integration;

import com.jaspersoft.android.sdk.client.async.request.CheckReportStatusRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;
import com.jaspersoft.android.sdk.integration.utils.ProtoInstrumentation;
import com.jaspersoft.android.sdk.integration.utils.SampleData;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class CheckReportStatusRequestTest extends ProtoInstrumentation {

    private RunReportExecutionRequest runReportExecutionRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ReportExecutionRequest reportExecutionRequest = SampleData.getSampleExecutionData(getJsRestClient(), RESOURCE_URI);
        runReportExecutionRequest = new RunReportExecutionRequest(getJsRestClient(), reportExecutionRequest);
    }

    public void test_requestReportStatus() throws Exception {
        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        CheckReportStatusRequest checkReportStatusRequest = new CheckReportStatusRequest(getJsRestClient(), requestId);
        ReportStatusResponse response = checkReportStatusRequest.loadDataFromNetwork();
        assertFalse(response.getStatus() == null);
    }

}
