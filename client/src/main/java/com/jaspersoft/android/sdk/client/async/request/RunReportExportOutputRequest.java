package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.report.ReportDataResponse;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class RunReportExportOutputRequest extends BaseRequest<ReportDataResponse> {
    private String requestId;
    private String executionId;

    public RunReportExportOutputRequest(JsRestClient jsRestClient) {
        super(jsRestClient, ReportDataResponse.class);
    }

    public RunReportExportOutputRequest(JsRestClient jsRestClient, String requestId, String executionId) {
        super(jsRestClient, ReportDataResponse.class);
        this.requestId = requestId;
        this.executionId = executionId;
    }

    @Override
    public ReportDataResponse loadDataFromNetwork() throws Exception {
        return getJsRestClient().runExportOutputResource(requestId, executionId);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
