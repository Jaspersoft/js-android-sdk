package com.jaspersoft.android.sdk.client.util;

import com.jaspersoft.android.sdk.client.JsServerProfile;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class JsServerProfileAdapter {
    public static JsServerProfileAdapter newInstance() {
        return new JsServerProfileAdapter();
    }

    public JsServerProfile adapt(ServerUnderTest serverUnderTest) {
        JsServerProfile adaptee = new JsServerProfile();
        adaptee.setAlias(serverUnderTest.getAlias());
        adaptee.setServerUrl(serverUnderTest.getServerUrl());
        adaptee.setOrganization(serverUnderTest.getOrganization());
        adaptee.setUsername(serverUnderTest.getUsername());
        adaptee.setPassword(serverUnderTest.getPassword());
        adaptee.setVersionCode(serverUnderTest.getVersionCode());
        adaptee.setServerEdition(serverUnderTest.getServerEdition());
        return adaptee;
    }
}
