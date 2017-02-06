/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthStrategyTest {

    @Mock
    SpringAuthServiceFactory springAuthServiceFactory;
    @Mock
    SingleSignOnServiceFactory singleSignOnServiceFactory;
    @Mock
    PreAuthenticationServiceFactory preAuthenticationServiceFactory;
    @Mock
    SpringAuthService springAuthService;
    @Mock
    AuthenticationLifecycle authenticationLifecycle;

    private AuthStrategy authStrategy;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(springAuthServiceFactory.create()).thenReturn(springAuthService);
        authStrategy = new AuthStrategy(springAuthServiceFactory, preAuthenticationServiceFactory, singleSignOnServiceFactory, authenticationLifecycle);
    }

    @Test
    public void testApply() throws Exception {
        SpringCredentials springCredentials = SpringCredentials.builder()
                .withUsername("user")
                .withPassword("1234")
                .build();

        authStrategy.apply(springCredentials);
        verify(springAuthServiceFactory).create();

        authStrategy.apply(springCredentials);
        verifyNoMoreInteractions(springAuthServiceFactory);

        verify(authenticationLifecycle, times(2)).beforeSessionReload();
        verify(authenticationLifecycle, times(2)).afterSessionReload();
    }
}