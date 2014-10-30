package com.jaspersoft.android.sdk.integration.utils;

import android.test.InstrumentationTestCase;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.JsServerProfile;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public abstract class ProtoInstrumentation extends InstrumentationTestCase {

    private static final String DEFAULT_ALIAS = "Mobile Demo";
    private static final String DEFAULT_ORGANIZATION = "";
    private static final String DEFAULT_SERVER_URL = "http://build-master.jaspersoft.com:6680/jrs-pro-feature-visualizejs";
    private static final String DEFAULT_USERNAME = "superuser";
    private static final String DEFAULT_PASS = "superuser";

    public static final String RESOURCE_URI = "/public/Samples/Reports/AllAccounts";

    private JsServerProfile testProfile;
    private JsRestClient jsRestClient;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testProfile = new JsServerProfile();
        testProfile.setAlias(DEFAULT_ALIAS);
        testProfile.setServerUrl(DEFAULT_SERVER_URL);
        testProfile.setOrganization(DEFAULT_ORGANIZATION);
        testProfile.setUsername(DEFAULT_USERNAME);
        testProfile.setPassword(DEFAULT_PASS);

        jsRestClient = new JsRestClient();
        jsRestClient.setServerProfile(testProfile);


    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testProfile = null;
        jsRestClient = null;
    }

    public JsServerProfile getTestProfile() {
        return testProfile;
    }

    public JsRestClient getJsRestClient() {
        return jsRestClient;
    }

}
