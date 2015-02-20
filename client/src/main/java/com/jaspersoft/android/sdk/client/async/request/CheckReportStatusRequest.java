package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;

/**
 * @author Tom Koptel
 * @since 1.9
 *
 * Check the status of export execution.
 */
public class CheckReportStatusRequest extends BaseRequest<ReportStatusResponse> {
    private String executionId;

    /**
     *
     * @param jsRestClient Rest client. Encapsulates call logic
     * @param executionId Current report identifier.
     */
    public CheckReportStatusRequest(JsRestClient jsRestClient, String executionId) {
        super(jsRestClient, ReportStatusResponse.class);
        this.executionId = executionId;
    }

    public CheckReportStatusRequest(JsRestClient jsRestClient) {
        super(jsRestClient, ReportStatusResponse.class);
    }

    @Override
    public ReportStatusResponse loadDataFromNetwork() throws Exception {
        return getJsRestClient().runReportStatusCheck(executionId);
    }

    public String getExportExecutionId() {
        return executionId;
    }

    public void setExportExecutionId(String exportExecutionId) {
        this.executionId = exportExecutionId;
    }
}
