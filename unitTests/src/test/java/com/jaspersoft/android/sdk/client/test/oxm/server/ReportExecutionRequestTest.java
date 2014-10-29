package com.jaspersoft.android.sdk.client.test.oxm.server;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.JsServerProfile;
import com.jaspersoft.android.sdk.client.oxm.report.ReportExecutionRequest;
import com.jaspersoft.android.sdk.client.test.support.UnitTestSpecification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportExecutionRequestTest extends UnitTestSpecification {
    private static final String SERVER_URL = "http://build-master.jaspersoft.com";

    @Mock
    JsRestClient jsRestClient;
    @Mock
    JsServerProfile jsServerProfile;

    ReportExecutionRequest executionRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(jsRestClient.getServerProfile()).thenReturn(jsServerProfile);
        when(jsServerProfile.getServerUrl()).thenReturn(SERVER_URL);
        executionRequest = new ReportExecutionRequest();
    }

    @Test
    public void test_ConfigureExecutionForProfileMethod_MutatesAttachmentsPrefix() throws UnsupportedEncodingException {
        executionRequest.configureExecutionForProfile(jsRestClient);
        String attachmentsPrefix = "http://build-master.jaspersoft.com/rest_v2/reportExecutions/{reportExecutionId}/exports/{exportExecutionId}/attachments/";
        assertThat(executionRequest.getAttachmentsPrefix(), is(attachmentsPrefix));
    }

    @Test
    public void test_ConfigureExecutionForProfileMethod_MutatesBaseUrl() {
        executionRequest.configureExecutionForProfile(jsRestClient);
        assertThat(executionRequest.getBaseUrl(), is(SERVER_URL));
    }
}
