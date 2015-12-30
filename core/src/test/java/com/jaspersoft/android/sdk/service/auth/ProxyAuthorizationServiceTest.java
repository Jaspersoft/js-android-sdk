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

package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthorizationClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProxyAuthorizationServiceTest {

    @Mock
    AuthorizationClient mAuthorizationClient;
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

    private ProxyAuthorizationService authorizationService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        authorizationService = new ProxyAuthorizationService(mAuthorizationClient, mServiceExceptionMapper);
    }

    @Test
    public void testAuthorize() throws Exception {
        authorizationService.authorize(mCredentials);
        verify(mAuthorizationClient).authorize(mCredentials);
        verifyZeroInteractions(mServiceExceptionMapper);
    }

    @Test
    public void testShouldMapHttpException() throws Exception {
        when(mServiceExceptionMapper.transform(any(HttpException.class))).thenReturn(mServiceException);
        doThrow(mHttpException).when(mAuthorizationClient).authorize(any(Credentials.class));

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
        doThrow(mIOException).when(mAuthorizationClient).authorize(any(Credentials.class));

        try {
            authorizationService.authorize(mCredentials);
            fail("Should fail with service exception");
        } catch (ServiceException ex) {
            assertThat(ex, is(mServiceException));
        }
    }
}