package com.jaspersoft.android.sdk.integration;

import android.support.test.runner.AndroidJUnit4;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.CheckReportStatusRequest;
import com.jaspersoft.android.sdk.client.async.request.RunReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;
import com.jaspersoft.android.sdk.integration.utils.FactoryGirl;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Tom Koptel
 * @since 1.9
 */
@RunWith(AndroidJUnit4.class)
public class CheckReportStatusRequestTest extends TestCase {

    private RunReportExecutionRequest runReportExecutionRequest;
    private JsRestClient jsRestClient;

    @Before
    public void setUp() throws Exception {
        FactoryGirl factoryGirl = FactoryGirl.newInstance();
        jsRestClient = factoryGirl.createJsRestClient();
        ReportExecutionRequest reportExecutionRequest = factoryGirl.createExecutionData(jsRestClient);
        runReportExecutionRequest = new RunReportExecutionRequest(jsRestClient, reportExecutionRequest);
    }

    @Test
    public void test_requestReportStatus() throws Exception {
        ReportExecutionResponse runReportExecutionResponse = runReportExecutionRequest.loadDataFromNetwork();
        String requestId = runReportExecutionResponse.getRequestId();

        CheckReportStatusRequest checkReportStatusRequest = new CheckReportStatusRequest(jsRestClient, requestId);
        ReportStatusResponse response = checkReportStatusRequest.loadDataFromNetwork();
        assertThat(response.getStatus(), notNullValue());
    }

}
