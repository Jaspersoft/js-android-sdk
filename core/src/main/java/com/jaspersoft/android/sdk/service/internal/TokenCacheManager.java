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
import com.jaspersoft.android.sdk.service.RestClient;
import com.jaspersoft.android.sdk.service.Session;
import com.jaspersoft.android.sdk.service.auth.AuthenticationService;
import com.jaspersoft.android.sdk.service.auth.Credentials;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.token.TokenCache;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class TokenCacheManager {
    private final AuthenticationService mAuthService;
    private final Credentials mCredentials;
    private final TokenCache mTokenCache;
    private final String mBaseUrl;

    TokenCacheManager(AuthenticationService authService,
                      Credentials credentials,
                      TokenCache tokenCache,
                      String baseUrl) {
        mAuthService = authService;
        mCredentials = credentials;
        mTokenCache = tokenCache;
        mBaseUrl = baseUrl;
    }

    @NotNull
    public static TokenCacheManager create(RestClient restClient, Session session) {
        AuthenticationService service = session.authApi();
        Credentials credentials = session.getCredentials();
        TokenCache cache = session.getTokenCache();
        String baseUrl = restClient.getServerUrl();
        return new TokenCacheManager(service, credentials, cache, baseUrl);
    }

    @NotNull
    public String loadToken() throws IOException, HttpException {
        String token = mTokenCache.get(mBaseUrl);
        if (token != null) {
            return token;
        }

        try {
            token = mAuthService.authenticate(mCredentials);
            mTokenCache.put(mBaseUrl, token);
            return token;
        } catch (ServiceException e) {
            if (e.getCause() instanceof HttpException) {
                throw (HttpException) e.getCause();
            }
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            }
            throw new RuntimeException("Failed to load token", e.getCause());
        }
    }

    public void invalidateToken() {
        mTokenCache.remove(mBaseUrl);
    }
}
