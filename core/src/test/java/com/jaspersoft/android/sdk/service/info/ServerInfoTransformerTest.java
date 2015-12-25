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

package com.jaspersoft.android.sdk.service.info;

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.service.info.ServerInfoTransformer;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServerInfoData.class})
public class ServerInfoTransformerTest {

    ServerInfoData mServerInfoData;

    private ServerInfoTransformer transformerUnderTest;

    @Before
    public void setup() {
        transformerUnderTest = ServerInfoTransformer.get();
        mServerInfoData = PowerMockito.mock(ServerInfoData.class);
        when(mServerInfoData.getBuild()).thenReturn("20150527_1447");
        when(mServerInfoData.getDateFormatPattern()).thenReturn("yyyy-MM-dd");
        when(mServerInfoData.getDatetimeFormatPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss");
        when(mServerInfoData.getVersion()).thenReturn("6.1");
        when(mServerInfoData.getEdition()).thenReturn("PRO");
        when(mServerInfoData.getEditionName()).thenReturn("Enterprise for AWS");
        when(mServerInfoData.getFeatures()).thenReturn("Fusion");
        when(mServerInfoData.getLicenseType()).thenReturn("Type");
    }

    @Test
    public void shouldTransform() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoData);
        assertThat(info.getBuild(), is("20150527_1447"));
        assertThat(info.getDateFormatPattern(), is(new SimpleDateFormat("yyyy-MM-dd")));
        assertThat(info.getDatetimeFormatPattern(), is(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(info.getVersion(), is(ServerVersion.v6_1));
        assertThat(info.isEditionPro(), is(true));
        assertThat(info.getEditionName(), is("Enterprise for AWS"));
        assertThat(info.getFeatures(), contains("Fusion"));
        assertThat(info.getLicenseType(), is("Type"));
    }
}
