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
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.test.Chain;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class DefaultCallExecutorTest {

    @Mock
    TokenCacheManager mTokenCacheManager;
    @Mock
    Call<?> mCall;

    @Mock
    HttpException _401Exception;

    @Mock
    ServerInfo mServerInfo;

    private DefaultCallExecutor resolver;
    private Object mResponse = new Object();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(_401Exception.code()).thenReturn(401);
        when(mCall.perform(anyString())).thenReturn(mResponse);

        resolver = new DefaultCallExecutor(mTokenCacheManager, new DefaultExceptionMapper());
    }

    @Test
    public void testExecuteWithValidCache() throws Exception {
        when(mTokenCacheManager.loadToken()).thenReturn("token");

        assertThat("Failed to return response from call operation", resolver.execute(mCall), is(mResponse));

        verify(mTokenCacheManager).loadToken();
        verify(mCall).perform("token");
        verifyNoMoreInteractions(mTokenCacheManager);
        verifyZeroInteractions(mTokenCacheManager);
    }

    @Test
    public void testExecuteWithEmptyCache() throws Exception {
        when(mTokenCacheManager.loadToken()).thenReturn(null);
        when(mTokenCacheManager.loadToken()).thenReturn("token");

        assertThat("Failed to return response from call operation", resolver.execute(mCall), is(mResponse));

        verify(mTokenCacheManager).loadToken();
        verify(mCall).perform("token");
    }

    @Test
    public void testExecuteWithInvalidCache() throws Exception {
        when(mTokenCacheManager.loadToken()).then(Chain.of("invalid token", "valid token"));

        when(_401Exception.code()).thenReturn(401);
        when(mCall.perform(anyString())).thenAnswer(_401ResponseAtFirstInvokation());

        assertThat("Failed to return response from call operation", resolver.execute(mCall), is(mResponse));

        verify(mTokenCacheManager, times(2)).loadToken();
        verify(mCall).perform("invalid token");
        verify(mTokenCacheManager).invalidateToken();
        verify(mCall).perform("valid token");
    }

    @Test
    public void testExecuteWithInvalidCredentials() throws Exception {
        when(mTokenCacheManager.loadToken()).thenReturn("invalid token");
        when(mCall.perform(anyString())).thenThrow(_401Exception);

        try {
            resolver.execute(mCall);
        } catch (ServiceException exception) {
            assertThat(exception.code(), is(StatusCodes.AUTHORIZATION_ERROR));
        }

        verify(mTokenCacheManager, times(2)).loadToken();
        verify(mCall, times(2)).perform("invalid token");
        verify(mTokenCacheManager).invalidateToken();
    }

    @Test
    public void testExecuteWithInvalidCredentialsAndEmptyCache() throws Exception {
        when(mTokenCacheManager.loadToken()).thenThrow(_401Exception);

        try {
            resolver.execute(mCall);
        } catch (ServiceException exception) {
            assertThat(exception.code(), is(StatusCodes.AUTHORIZATION_ERROR));
        }

        verify(mTokenCacheManager).invalidateToken();
        verify(mTokenCacheManager, times(2)).loadToken();
        verifyNoMoreInteractions(mTokenCacheManager);
        verifyNoMoreInteractions(mTokenCacheManager);
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