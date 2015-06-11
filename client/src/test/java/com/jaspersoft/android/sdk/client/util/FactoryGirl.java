package com.jaspersoft.android.sdk.client.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.JsServerProfile;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParametersList;
import com.jaspersoft.android.sdk.client.oxm.report.adapter.ReportParametersListDeserializer;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookupSearchCriteria;

import java.util.ArrayList;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class FactoryGirl {
    private static final String ATTACHMENT_PREFIX_5_6 = "/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/";
    private static final String ATTACHMENT_PREFIX_5_0 = "/reportExecutions/{reportExecutionId}/exports/{exportOptions}/attachments/";

    public static FactoryGirl newInstance() {
        return new FactoryGirl();
    }

    public JsRestClient createJsRestClient(String dataType) {
        return createJsRestClient(dataType, createJsServerProfile());
    }

    public JsRestClient createJsRestClient(ServerUnderTest serverUnderTest) {
        return createJsRestClient(JsRestClient.DataType.XML.toString(), JsServerProfileAdapter.newInstance().adapt(serverUnderTest));
    }

    public JsRestClient createJsRestClient(String dataType, ServerUnderTest serverUnderTest) {
        return createJsRestClient(dataType, JsServerProfileAdapter.newInstance().adapt(serverUnderTest));
    }

    public JsRestClient createJsRestClient(JsServerProfile jsServerProfile) {
        return createJsRestClient(JsRestClient.DataType.XML.toString(), jsServerProfile);
    }

    public JsRestClient createJsRestClient(String dataType, JsServerProfile jsServerProfile) {
        JsRestClient jsRestClient = JsRestClient
                .builder()
                .setDataType(JsRestClient.DataType.valueOf(dataType))
                .build();
        jsRestClient.setServerProfile(jsServerProfile);
        return jsRestClient;
    }

    public JsServerProfile createJsServerProfile() {
        return JsServerProfileAdapter
                .newInstance()
                .adapt(ServerUnderTest.createDefault());
    }

    public ReportExecutionRequest createExecutionData(JsRestClient jsRestClient) {
        return createExecutionData(
                jsRestClient,
                ResourceUnderTestFactory
                        .newInstance(jsRestClient.getServerProfile())
                        .create()
        );
    }

    public ReportExecutionRequest createExecutionData(JsRestClient jsRestClient, ResourceUnderTest resourceUnderTest) {
        ReportExecutionRequest reportExecutionRequest = new ReportExecutionRequest();

        JsServerProfile jsServerProfile = jsRestClient.getServerProfile();
        String serverUrl = jsServerProfile.getServerUrl();
        String attachmentsPrefix = (jsServerProfile.getServerUrl() + JsRestClient.REST_SERVICES_V2_URI + resourceUnderTest.getAttachmentPrefix());
        reportExecutionRequest.setAttachmentsPrefix(attachmentsPrefix);
        reportExecutionRequest.setBaseUrl(serverUrl);

        reportExecutionRequest.setReportUnitUri(resourceUnderTest.getUri());
        reportExecutionRequest.setMarkupType(ReportExecutionRequest.MARKUP_TYPE_EMBEDDABLE);
        reportExecutionRequest.setOutputFormat("HTML");
        reportExecutionRequest.setPages("1");
        reportExecutionRequest.setAsync(true);
        reportExecutionRequest.setInteractive(true);
        reportExecutionRequest.setFreshData(false);
        reportExecutionRequest.setSaveDataSnapshot(false);
        reportExecutionRequest.setAllowInlineScripts(false);

        String json = TestResource.getJson().rawData("report_parameters");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ReportParametersList.class, new ReportParametersListDeserializer());
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        ReportParametersList reportParameter = gson.fromJson(json, ReportParametersList.class);
        reportExecutionRequest.setParameters(reportParameter.getReportParameters());

        return reportExecutionRequest;
    }

    public String getResourceUri(JsRestClient jsRestClient) {
        return ResourceUnderTestFactory
                .newInstance(jsRestClient.getServerProfile())
                .create().getUri();
    }

    public ResourceLookupSearchCriteria createSearchCriteria() {
        ResourceLookupSearchCriteria searchCriteria = new ResourceLookupSearchCriteria();
        searchCriteria.setForceFullPage(true);
        searchCriteria.setLimit(10);
        searchCriteria.setRecursive(true);
        searchCriteria.setTypes(new ArrayList<String>() {
            {
                add(ResourceLookup.ResourceType.dashboard.toString());
                add(ResourceLookup.ResourceType.legacyDashboard.toString());
            }
        });
        searchCriteria.setFolderUri("/");
        searchCriteria.setSortBy("label");
        return searchCriteria;
    }
}
