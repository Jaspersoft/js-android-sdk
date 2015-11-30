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

package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.auth.Credentials;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CallExecutorTest {

    @Mock
    TokenCache mCache;
    @Mock
    TokenFactory mFactory;
    @Mock
    Credentials mCredentials;
    @Mock
    Call<?> mCall;

    @Mock
    HttpException _401Exception;

    private CallExecutor resolver;
    private Object mResponse = new Object();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(_401Exception.code()).thenReturn(401);
        when(mCall.perform(anyString())).thenReturn(mResponse);
        resolver = new CallExecutor(mCredentials, mCache, mFactory);
    }

    @Test
    public void testExecuteWithValidCache() throws Exception {
        when(mCache.get(any(Credentials.class))).thenReturn("token");

        assertThat("Failed to return response from call operation", resolver.execute(mCall), is(mResponse));

        verify(mCache).get(mCredentials);
        verify(mCall).perform("token");
        verifyNoMoreInteractions(mCache);
        verifyZeroInteractions(mFactory);
    }

    @Test
    public void testExecuteWithEmptyCache() throws Exception {
        when(mCache.get(any(Credentials.class))).thenReturn(null);
        when(mFactory.create(any(Credentials.class))).thenReturn("token");

        assertThat("Failed to return response from call operation", resolver.execute(mCall), is(mResponse));

        verify(mCache).get(mCredentials);
        verify(mFactory).create(mCredentials);
        verify(mCache).put(mCredentials, "token");
        verify(mCall).perform("token");
    }

    @Test
    public void testExecuteWithInvalidCache() throws Exception {
        when(mCache.get(any(Credentials.class))).thenReturn("invalid token");

        when(_401Exception.code()).thenReturn(401);
        when(mCall.perform(anyString())).thenAnswer(_401ResponseAtFirstInvokation());

        when(mFactory.create(any(Credentials.class))).thenReturn("token");

        assertThat("Failed to return response from call operation", resolver.execute(mCall), is(mResponse));

        verify(mCache).get(mCredentials);
        verify(mCall).perform("invalid token");
        verify(mCache).evict(mCredentials);
        verify(mFactory).create(mCredentials);
        verify(mCache).put(mCredentials, "token");
        verify(mCall).perform("token");
    }

    @Test
    public void testExecuteWithInvalidCredentials() throws Exception {
        when(mCache.get(any(Credentials.class))).thenReturn("invalid token");
        when(mCall.perform(anyString())).thenThrow(_401Exception);
        when(mFactory.create(any(Credentials.class))).thenThrow(_401Exception);

        try {
            resolver.execute(mCall);
        } catch (ServiceException exception) {
            assertThat(exception.code(), is(StatusCodes.AUTHORIZATION_ERROR));
        }

        verify(mCache).get(mCredentials);
        verify(mCall).perform("invalid token");
        verify(mCache).evict(mCredentials);
        verify(mFactory).create(mCredentials);
    }

    @Test
    public void testExecuteWithInvalidCredentialsAndEmptyCache() throws Exception {
        when(mCache.get(any(Credentials.class))).thenReturn(null);
        when(mFactory.create(any(Credentials.class))).thenThrow(_401Exception);

        try {
            resolver.execute(mCall);
        } catch (ServiceException exception) {
            assertThat(exception.code(), is(StatusCodes.AUTHORIZATION_ERROR));
        }

        verify(mCache).get(mCredentials);
        verify(mCache).evict(mCredentials);

        verify(mFactory, times(2)).create(mCredentials);
        verifyNoMoreInteractions(mCache);
        verifyNoMoreInteractions(mFactory);
        verifyZeroInteractions(mCall);
    }

    private Answer<Object> _401ResponseAtFirstInvokation() {
        return new Answer<Object>() {
            int invocationCount = 0;
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (invocationCount == 0) {
                    invocationCount++;
                    throw _401Exception;
                }
                return mResponse;
            }
        };
    }
}