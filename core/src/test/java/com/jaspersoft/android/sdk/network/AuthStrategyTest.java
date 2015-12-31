/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthStrategyTest {

    @Mock
    SpringAuthServiceFactory mSpringAuthServiceFactory;
    @Mock
    SpringAuthService mSpringAuthService;

    private AuthStrategy authStrategy;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mSpringAuthServiceFactory.create()).thenReturn(mSpringAuthService);
        authStrategy = new AuthStrategy(mSpringAuthServiceFactory);
    }

    @Test
    public void testApply() throws Exception {
        SpringCredentials springCredentials = SpringCredentials.builder()
                .withUsername("user")
                .withPassword("1234")
                .build();
        authStrategy.apply(springCredentials);
        verify(mSpringAuthServiceFactory).create();

        authStrategy.apply(springCredentials);
        verifyNoMoreInteractions(mSpringAuthServiceFactory);
    }
}