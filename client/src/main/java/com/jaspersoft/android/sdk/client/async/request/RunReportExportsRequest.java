package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.report.ExportExecution;
import com.jaspersoft.android.sdk.client.oxm.report.ExportsRequest;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class RunReportExportsRequest extends BaseRequest<ExportExecution>  {

    private ExportsRequest request;
    private String executionId;

    public RunReportExportsRequest(JsRestClient jsRestClient) {
        super(jsRestClient, ExportExecution.class);
    }

    public RunReportExportsRequest(JsRestClient jsRestClient,
                                   ExportsRequest request, String executionId) {
        super(jsRestClient, ExportExecution.class);
        this.request = request;
        this.executionId = executionId;
    }

    @Override
    public ExportExecution loadDataFromNetwork() throws Exception {
        return getJsRestClient().runExportForReport(executionId, request);
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public ExportsRequest getRequest() {
        return request;
    }

    public void setRequest(ExportsRequest request) {
        this.request = request;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }
}
