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

package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.server.EncryptionKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Locale.class,
        EncryptionKey.class,
        JSEncryptionAlgorithm.class,
})
public class SpringAuthServiceTest {
    @Mock
    AuthRestApi mRestApi;
    @Mock
    JSEncryptionAlgorithm mAlgorithm;
    @Mock
    EncryptionKey mKey;
    @Mock
    TimeZone mTimeZone;

    private SpringAuthService objectUnderTest;
    private SpringCredentials credentials;

    private static final Map<String, String> sOptionals = new HashMap<>();

    static {
        sOptionals.put("userLocale", "en_US");
        sOptionals.put("userTimezone", "Europe/Helsinki");
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SpringAuthService(
                mRestApi,
                mAlgorithm
        );

        credentials = SpringCredentials.builder()
                .withUsername("user")
                .withPassword("1234")
                .withOrganization("organization")
                .withLocale(Locale.US)
                .withTimeZone(mTimeZone)
                .build();

        when(mRestApi.requestEncryptionMetadata()).thenReturn(mKey);
        when(mTimeZone.getID()).thenReturn("Europe/Helsinki");
    }

    @Test
    public void shouldAuthenticateWithHashedPasswordIfEncryptionKeyIsMissing() throws Exception {
        when(mKey.isAvailable()).thenReturn(true);
        when(mKey.getExponent()).thenReturn("e");
        when(mKey.getModulus()).thenReturn("m");
        when(mAlgorithm.encrypt(anyString(), anyString(), anyString())).thenReturn("hashed password");

        objectUnderTest.authenticate(credentials);

        verify(mRestApi, times(1)).springAuth("user", "hashed password", "organization", sOptionals);
        verify(mRestApi, times(1)).requestEncryptionMetadata();
        verify(mAlgorithm, times(1)).encrypt("m", "e", "1234");
    }

    @Test
    public void shouldAuthenticateWithOpenPasswordIfEncryptionKeyIsMissing() throws Exception {
        when(mKey.isAvailable()).thenReturn(false);

        objectUnderTest.authenticate(credentials);

        verify(mRestApi, times(1)).springAuth("user", "1234", "organization", sOptionals);
        verify(mRestApi, times(1)).requestEncryptionMetadata();
        verifyZeroInteractions(mAlgorithm);
    }
}