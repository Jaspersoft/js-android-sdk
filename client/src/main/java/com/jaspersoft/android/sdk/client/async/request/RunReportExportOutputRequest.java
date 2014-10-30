package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class RunReportExportOutputRequest extends BaseRequest<String> {
    private String requestId;
    private String executionId;

    public RunReportExportOutputRequest(JsRestClient jsRestClient) {
        super(jsRestClient, String.class);
    }

    public RunReportExportOutputRequest(JsRestClient jsRestClient, String requestId, String executionId) {
        super(jsRestClient, String.class);
        this.requestId = requestId;
        this.executionId = executionId;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
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
