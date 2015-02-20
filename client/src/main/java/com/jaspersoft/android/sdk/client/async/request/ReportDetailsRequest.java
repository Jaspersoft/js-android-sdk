package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionResponse;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportDetailsRequest extends BaseRequest<ReportExecutionResponse> {
    private String executionId;

    public ReportDetailsRequest(JsRestClient jsRestClient) {
        super(jsRestClient, ReportExecutionResponse.class);
    }

    public ReportDetailsRequest(JsRestClient jsRestClient, String executionId) {
        super(jsRestClient, ReportExecutionResponse.class);
        this.executionId = executionId;
    }

    @Override
    public ReportExecutionResponse loadDataFromNetwork() throws Exception {
        return getJsRestClient().runReportDetailsRequest(executionId);
    }
}
