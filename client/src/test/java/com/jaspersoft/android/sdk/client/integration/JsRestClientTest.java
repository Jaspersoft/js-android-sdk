package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.BuildConfig;
import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.JsServerProfile;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 1.9
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@Ignore
public class JsRestClientTest {
    private static final String SERVER_URL = "http://build-master.jaspersoft.com/jasperserver-pro";
    private static final String REQUEST_ID = "da977e74-561a-47ac-a92f-f3f3d98aac72";

    private JsRestClient jsRestClient;
    @Mock
    private JsServerProfile jsServerProfile;

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
