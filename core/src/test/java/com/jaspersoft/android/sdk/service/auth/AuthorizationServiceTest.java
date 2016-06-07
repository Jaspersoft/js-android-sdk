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

package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.network.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthorizationServiceTest {

    @Mock
    AnonymousClient mAnonymousClient;
    @Mock
    AuthenticationRestApi mAuthenticationRestApi;
    @Mock
    ServiceExceptionMapper mServiceExceptionMapper;
    @Mock
    Credentials mCredentials;

    @Mock
    HttpException mHttpException;
    @Mock
    IOException mIOException;
    @Mock
    ServiceException mServiceException;

    @Rule
    public ExpectedException expected = none();

    private AuthorizationService authorizationService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(mAnonymousClient.authenticationApi()).thenReturn(mAuthenticationRestApi);
        authorizationService = new AuthorizationService(mAnonymousClient, mServiceExceptionMapper);
    }

    @Test
    public void testAuthorize() throws Exception {
        authorizationService.authorize(mCredentials);
        verify(mAnonymousClient).authenticationApi();
        verify(mAuthenticationRestApi).authenticate(mCredentials);
        verifyZeroInteractions(mServiceExceptionMapper);
    }

    @Test
    public void testShouldMapHttpException() throws Exception {
        when(mServiceExceptionMapper.transform(any(HttpException.class))).thenReturn(mServiceException);
        doThrow(mHttpException).when(mAuthenticationRestApi).authenticate(any(Credentials.class));

        try {
            authorizationService.authorize(mCredentials);
            fail("Should fail with service exception");
        } catch (ServiceException ex) {
            assertThat(ex, is(mServiceException));
        }
    }

    @Test
    public void testShouldMapIOException() throws Exception {
        when(mServiceExceptionMapper.transform(any(IOException.class))).thenReturn(mServiceException);
        doThrow(mIOException).when(mAuthenticationRestApi).authenticate(any(Credentials.class));

        try {
            authorizationService.authorize(mCredentials);
            fail("Should fail with service exception");
        } catch (ServiceException ex) {
            assertThat(ex, is(mServiceException));
        }
    }

    @Test
    public void should_not_allow_null_client() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Client should not be null");
        AuthorizationService.newService(null);
    }
}