package com.jaspersoft.android.sdk.integration.utils;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.JsServerProfile;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class FactoryGirl {
    public static final String RESOURCE_URI = "/Reports/1._Geographic_Results_by_Segment_Report";
    private static final String ATTACHMENT_PREFIX_5_6 = "/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/";
    private static final String ATTACHMENT_PREFIX_5_0 = "/reportExecutions/{reportExecutionId}/exports/{exportOptions}/attachments/";

    public static FactoryGirl newInstance() {
        return new FactoryGirl();
    }

    public JsRestClient createJsRestClient() {
        return createJsRestClient(createJsServerProfile());
    }

    public JsRestClient createJsRestClient(ServerUnderTest serverUnderTest) {
        return createJsRestClient(JsServerProfileAdapter.newInstance().adapt(serverUnderTest));
    }

    public JsRestClient createJsRestClient(JsServerProfile jsServerProfile) {
        JsRestClient jsRestClient = new JsRestClient();
        jsRestClient.setServerProfile(jsServerProfile);
        return jsRestClient;
    }

    public JsServerProfile createJsServerProfile() {
        return JsServerProfileAdapter
                .newInstance()
                .adapt(ServerUnderTest.createDefault());
    }

    public ReportExecutionRequest createExecutionData(JsRestClient jsRestClient) {
        return createExecutionData(jsRestClient, getResourceUri());
    }

    public ReportExecutionRequest createExecutionData(JsRestClient jsRestClient, String resourceUri) {
        ReportExecutionRequest reportExecutionRequest = new ReportExecutionRequest();

        String prefix = ATTACHMENT_PREFIX_5_6;
        JsServerProfile jsServerProfile = jsRestClient.getServerProfile();
        String serverUrl = jsServerProfile.getServerUrl();
        String attachmentsPrefix = (jsServerProfile.getServerUrl() + JsRestClient.REST_SERVICES_V2_URI + prefix);
        reportExecutionRequest.setAttachmentsPrefix(attachmentsPrefix);
        reportExecutionRequest.setBaseUrl(serverUrl);

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

    public String getResourceUri() {
        return RESOURCE_URI;
    }
}
