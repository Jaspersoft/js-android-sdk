package com.jaspersoft.android.sdk.integration;

import android.test.suitebuilder.annotation.LargeTest;

import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExportsRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ExportExecution;
import com.jaspersoft.android.sdk.client.oxm.report.ExportsRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;

/**
 * @author Tom Koptel
 * @since 1.9
 */
@LargeTest
public class RunReportExportsRequestTest extends ProtoInstrumentation {

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

        ExportsRequest exr = new ExportsRequest();
        exr.configureExecutionForProfile(getJsRestClient());
        exr.setMarkupType(ReportExecutionRequest.MARKUP_TYPE_EMBEDDABLE);
        exr.setAllowInlineScripts(false);
        exr.setOutputFormat("html");
        exr.setPages("1");

        RunReportExportsRequest runReportExportsRequest = new RunReportExportsRequest(getJsRestClient(), exr, requestId);
        ExportExecution runReportExportsResponse = runReportExportsRequest.loadDataFromNetwork();
        assertFalse(runReportExportsResponse == null);
    }

}
