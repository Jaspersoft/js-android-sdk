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

import com.jaspersoft.android.sdk.network.Cookies;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.auth.AuthenticationService;
import com.jaspersoft.android.sdk.service.auth.Credentials;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.token.TokenCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
        AuthenticationService.class
})
public class TokenCacheManagerTest {
    @Mock
    TokenCache mTokenCache;
    @Mock
    AuthenticationService mAuthenticationService;
    @Mock
    Credentials mCredentials;
    @Mock
    HttpException mHttpException;

    private final Cookies fakeCookies = Cookies.parse("key=value");
    private final String baseUrl = "http://localhost/";
    private TokenCacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cacheManager = new TokenCacheManager(
                mAuthenticationService,
                mCredentials,
                mTokenCache,
                baseUrl
        );
    }

    @Test
    public void testLoadToken() throws Exception {
        when(mTokenCache.get(anyString())).thenReturn(null);
        when(mAuthenticationService.authenticate(any(Credentials.class))).thenReturn(fakeCookies);

        assertThat("Cache manager has not returned 'token'", cacheManager.loadToken(), is(fakeCookies));

        verify(mTokenCache).get(baseUrl);
        verify(mTokenCache).put(baseUrl, fakeCookies);
        verify(mAuthenticationService).authenticate(mCredentials);
    }

    @Test
    public void testLoadTokenReturnsFromCache() throws Exception {
        when(mTokenCache.get(anyString())).thenReturn(fakeCookies);

        assertThat("Cache manager has not returned 'token'", cacheManager.loadToken(), is(fakeCookies));
        verify(mTokenCache).get(baseUrl);
        verifyNoMoreInteractions(mTokenCache);
        verifyZeroInteractions(mAuthenticationService);
    }

    @Test(expected = IOException.class)
    public void testLoadTokenRethrowsIOException() throws Exception {
        when(mTokenCache.get(anyString())).thenReturn(null);
        when(mAuthenticationService.authenticate(any(Credentials.class)))
                .thenThrow(new ServiceException("", new IOException(), 0));

        cacheManager.loadToken();
    }

    @Test(expected = HttpException.class)
    public void testLoadTokenRethrowsHttpException() throws Exception {
        when(mTokenCache.get(anyString())).thenReturn(null);
        when(mAuthenticationService.authenticate(any(Credentials.class)))
                .thenThrow(new ServiceException("", mHttpException, 0));

        cacheManager.loadToken();
    }

    @Test(expected = RuntimeException.class)
    public void testLoadTokenThrowsRuntimeForUnexpectedError() throws Exception {
        when(mTokenCache.get(anyString())).thenReturn(null);
        when(mAuthenticationService.authenticate(any(Credentials.class)))
                .thenThrow(new ServiceException("", new NullPointerException(), 0));

        cacheManager.loadToken();
    }

    @Test
    public void testInvalidateToken() throws Exception {
        cacheManager.invalidateToken();
        verify(mTokenCache).remove(baseUrl);
    }
}
