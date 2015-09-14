/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.server;

import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;
import com.jaspersoft.android.sdk.service.data.server.ServerEdition;
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
@PrepareForTest({ServerInfoResponse.class})
public class ServerInfoTransformerTest {

    ServerInfoResponse mServerInfoResponse;

    private ServerInfoTransformer transformerUnderTest;

    @Before
    public void setup() {
        transformerUnderTest = ServerInfoTransformer.getInstance();
        mServerInfoResponse = PowerMockito.mock(ServerInfoResponse.class);
        when(mServerInfoResponse.getBuild()).thenReturn("20150527_1447");
        when(mServerInfoResponse.getDateFormatPattern()).thenReturn("yyyy-MM-dd");
        when(mServerInfoResponse.getDatetimeFormatPattern()).thenReturn("yyyy-MM-dd'T'HH:mm:ss");
        when(mServerInfoResponse.getVersion()).thenReturn("6.1");
        when(mServerInfoResponse.getEdition()).thenReturn("PRO");
        when(mServerInfoResponse.getEditionName()).thenReturn("Enterprise for AWS");
        when(mServerInfoResponse.getFeatures()).thenReturn("Fusion");
    }

    @Test
    public void shouldTransformBuildProperty() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoResponse);
        assertThat(info.getBuild(), is("20150527_1447"));
    }

    @Test
    public void shouldTransformDateFormatProperty() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoResponse);
        assertThat(info.getDateFormatPattern(), is(new SimpleDateFormat("yyyy-MM-dd")));
    }

    @Test
    public void shouldTransformDateTimeFormatProperty() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoResponse);
        assertThat(info.getDatetimeFormatPattern(), is(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void shouldTransformServerVersionProperty() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoResponse);
        assertThat(info.getVersion(), is(ServerVersion.AMBER_MR2));
    }

    @Test
    public void shouldTransformServerEditionProperty() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoResponse);
        assertThat(info.getEdition(), is(ServerEdition.PRO));
    }

    @Test
    public void shouldTransformServerEditionNameProperty() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoResponse);
        assertThat(info.getEditionName(), is("Enterprise for AWS"));
    }

    @Test
    public void shouldTransformFeaturesProperty() {
        ServerInfo info = transformerUnderTest.transform(mServerInfoResponse);
        assertThat(info.getFeatures().asSet(), contains("Fusion"));
    }

}
