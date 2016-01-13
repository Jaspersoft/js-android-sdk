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

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.network.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ServerInfoData.class})
public class ServerInfoServiceTest {

    @Mock
    ServerRestApi mockApi;
    @Mock
    ServerInfoTransformer mockTransformer;
    @Mock
    ServerInfoData mockResponse;
    @Mock
    ServiceExceptionMapper mockServiceExceptionMapper;

    @Mock
    AnonymousClient mClient;

    @Rule
    public ExpectedException expected = none();

    private ServerInfoService serviceUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serviceUnderTest = new ServerInfoService(mockApi, mockTransformer, mockServiceExceptionMapper);
    }

    @Test
    public void requestInfoShouldProvideServerInfoDataObject() throws Exception {
        when(mockApi.requestServerInfo()).thenReturn(mockResponse);

        serviceUnderTest.requestServerInfo();

        verify(mockTransformer, times(1)).transform(mockResponse);
        verify(mockApi, times(1)).requestServerInfo();
    }

    @Test
    public void should_create_proxy_instance() throws Exception {
        ServerInfoService service = ServerInfoService.newService(mClient);
        assertThat("Should be instance of proxy service", service, is(instanceOf(ServerInfoService.class)));
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void should_reject_null_client() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);

        ServerInfoService.newService(null);
    }
}