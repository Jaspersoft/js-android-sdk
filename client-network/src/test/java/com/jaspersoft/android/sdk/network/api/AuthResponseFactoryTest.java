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

package com.jaspersoft.android.sdk.network.api;

import com.jaspersoft.android.sdk.network.entity.server.AuthResponse;
import com.squareup.okhttp.Headers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({com.squareup.okhttp.Response.class})
public class AuthResponseFactoryTest {
    @Mock
    com.squareup.okhttp.Response mResponse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldExtractTokenFromNetworkResponse() {
        Map<String, String> headersRaw = new HashMap<>();
        headersRaw.put("Set-Cookie", "cookie1");
        headersRaw.put("Set-Cookie", "cookie2");

        Headers headers = Headers.of(headersRaw);
        when(mResponse.headers()).thenReturn(headers);

        AuthResponse response = AuthResponseFactory.create(mResponse);
        assertThat(response.getToken(), is("cookie1;cookie2"));
    }
}
