package com.jaspersoft.android.sdk.integration.utils;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class SampleData {

    private SampleData() {}

    public static ReportExecutionRequest getSampleExecutionData(JsRestClient jsRestClient, String resourceUri) {
        ReportExecutionRequest reportExecutionRequest = new ReportExecutionRequest();
        reportExecutionRequest.configureExecutionForProfile(jsRestClient);
        reportExecutionRequest.setReportUnitUri(resourceUri);
        reportExecutionRequest.setMarkupType(ReportExecutionRequest.MARKUP_TYPE_EMBEDDABLE);
        reportExecutionRequest.setOutputFormat("HTML");
        reportExecutionRequest.setPages("1");
        reportExecutionRequest.setAsync(true);
        reportExecutionRequest.setInteractive(true);
        reportExecutionRequest.setFreshData(false);
        reportExecutionRequest.setSaveDataSnapshot(false);
        reportExecutionRequest.setAllowInlineScripts(false);
        return reportExecutionRequest;
    }

}
