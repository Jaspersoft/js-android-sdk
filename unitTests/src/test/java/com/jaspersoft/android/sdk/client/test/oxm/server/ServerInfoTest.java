package com.jaspersoft.android.sdk.client.test.oxm.server;

import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;
import com.jaspersoft.android.sdk.client.test.support.UnitTestSpecification;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ServerInfoTest extends UnitTestSpecification {

    ServerInfo mockServerInfo;

    @Before
    public void setMocks() {
        mockServerInfo = new ServerInfo();
    }

    @Test
    public void shouldParse() {
        mockServerInfo.setVersion("5.2.0");
        assertThat(mockServerInfo.getVersionCode(), is(50200)) ;
    }
}
