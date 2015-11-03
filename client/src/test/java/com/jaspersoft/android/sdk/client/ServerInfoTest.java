/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client;

import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ServerInfoTest {
    private ServerInfo mockServerInfo;

    @Before
    public void setMocks() {
        mockServerInfo = new ServerInfo();
    }

    @Test
    public void shouldParseSemanticVersioning() {
        Map<String, Double> doubleMap = new HashMap<String, Double>();
        doubleMap.put("5.0.0", new BigDecimal("5").doubleValue());
        doubleMap.put("5.1.0", new BigDecimal("5.1").doubleValue());
        doubleMap.put("5.2.0", new BigDecimal("5.2").doubleValue());
        doubleMap.put("5.5.0", new BigDecimal("5.5").doubleValue());
        doubleMap.put("5.6.0", new BigDecimal("5.6").doubleValue());
        doubleMap.put("5.6.1", new BigDecimal("5.61").doubleValue());
        doubleMap.put("6.0", new BigDecimal("6").doubleValue());

        for (Map.Entry<String, Double> entry : doubleMap.entrySet()) {
            mockServerInfo.setVersion(entry.getKey());
            assertThat(mockServerInfo.getVersionCode(), is(entry.getValue())) ;
        }
    }

    @Test
    public void shouldParseNonSemanticVersioning() {
        String[] nonSemanticOne = {"5.6.0 Preview", "5.6.0-BETA"};
        for (String nonSemanticVersion : nonSemanticOne) {
            mockServerInfo.setVersion(nonSemanticVersion);
            assertThat(mockServerInfo.getVersionCode(), is(ServerInfo.VERSION_CODES.EMERALD_THREE));
        }
    }

    @Test
    public void shouldParseLongSemanticVersioning() {
        Map<String, Double> doubleMap = new HashMap<String, Double>();
        doubleMap.put("5.6.1.2", new BigDecimal("5.612").doubleValue());
        doubleMap.put("5.6.1.2.0", new BigDecimal("5.612").doubleValue());
        doubleMap.put("5.5.6.1.2", new BigDecimal("5.5612").doubleValue());
        doubleMap.put("5.5.6.1.2.0", new BigDecimal("5.5612").doubleValue());
        doubleMap.put("5.5.6.1.2.3", new BigDecimal("5.56123").doubleValue());
        doubleMap.put("5.5.6.1.2.3.0", new BigDecimal("5.56123").doubleValue());

        for (Map.Entry<String, Double> entry : doubleMap.entrySet()) {
            mockServerInfo.setVersion(entry.getKey());
            assertThat(mockServerInfo.getVersionCode(), is(entry.getValue())) ;
        }
    }

}
