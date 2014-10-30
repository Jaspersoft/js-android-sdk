package com.jaspersoft.android.sdk.integration;

import com.jaspersoft.android.sdk.client.async.request.CheckReportStatusRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class CheckReportStatusRequestTest extends ProtoInstrumentation {

    private RunReportExecutionRequest runReportExecutionRequest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ReportExecutionRequest reportExecutionRequest = new ReportExecutionRequest();
        reportExecutionRequest.configureExecutionForProfile(getJsRestClient());
        reportExecutionRequest.setReportUnitUri(RESOURCE_URI);
        reportExecutionRequest.setMarkupType(ReportExecutionRequest.MARKUP_TYPE_EMBEDDABLE);
        reportExecutionRequest.setOutputFormat("HTML");
        reportExecutionRequest.setPages("1");
        reportExecutionRequest.setAsync(true);
        reportExecutionRequest.setInteractive(true);
        reportExecutionRequest.setFreshData(false);
        reportExecutionRequest.setSaveDataSnapshot(false);
        reportExecutionRequest.setAllowInlineScripts(false);

        runReportExecutionRequest = new RunReportExecutionRequest(getJsRestClient(), reportExecutionRequest);
    }

    public void test_requestForExportsOnReport() throws Exception {
        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        CheckReportStatusRequest checkReportStatusRequest = new CheckReportStatusRequest(getJsRestClient(), requestId);
        ReportStatusResponse response = checkReportStatusRequest.loadDataFromNetwork();
        assertFalse(response.getStatus() == null);
    }

}
