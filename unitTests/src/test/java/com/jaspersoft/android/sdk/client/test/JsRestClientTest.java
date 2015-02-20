package com.jaspersoft.android.sdk.client.test;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.JsServerProfile;
import com.jaspersoft.android.sdk.client.test.support.UnitTestSpecification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class JsRestClientTest extends UnitTestSpecification {

    private static final String SERVER_URL = "http://build-master.jaspersoft.com/jasperserver-pro";
    private static final String REQUEST_ID = "da977e74-561a-47ac-a92f-f3f3d98aac72";

    JsRestClient jsRestClient;
    @Mock
    JsServerProfile jsServerProfile;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsRestClient = new JsRestClient();
        when(jsServerProfile.getServerUrl()).thenReturn(SERVER_URL);
        when(jsServerProfile.getUsernameWithOrgId()).thenReturn("");
        when(jsServerProfile.getPassword()).thenReturn("");
        jsRestClient.setServerProfile(jsServerProfile);
    }

    @Test
    public void test_GetExportForReport_formsURI() {
        URI uri = jsRestClient.getExportForReportURI(REQUEST_ID);
        String expectedUri = "http://build-master.jaspersoft.com/jasperserver-pro/rest_v2/reportExecutions/da977e74-561a-47ac-a92f-f3f3d98aac72/exports";
        assertThat(uri.toString(), is(expectedUri));
    }
}
