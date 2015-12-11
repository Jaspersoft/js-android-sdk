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

package com.jaspersoft.android.sdk.service;

import com.jaspersoft.android.sdk.service.auth.Credentials;
import com.jaspersoft.android.sdk.service.report.ReportService;
import com.jaspersoft.android.sdk.service.repository.RepositoryService;
import com.jaspersoft.android.sdk.service.token.InMemoryTokenCache;
import com.jaspersoft.android.sdk.service.token.TokenCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Session extends AnonymousSession {

    private final Credentials mCredentials;
    private final TokenCache mTokenCache;

    private ReportService mReportService;
    private RepositoryService mRepositoryService;

    @TestOnly
    Session(RestClient client, Credentials credentials, TokenCache tokenCache) {
        super(client);
        mCredentials = credentials;
        mTokenCache = tokenCache;
    }

    public TokenCache getTokenCache() {
        return mTokenCache;
    }

    public Credentials getCredentials() {
        return mCredentials;
    }

    @NotNull
    public ReportService reportApi() {
        if (mReportService == null) {
            mReportService = ReportService.create(mClient, this);
        }
        return mReportService;
    }

    @NotNull
    public RepositoryService repositoryApi() {
        if (mRepositoryService == null) {
            mRepositoryService = RepositoryService.create(mClient, this);
        }
        return mRepositoryService;
    }

    public static class Builder {
        private final RestClient mClient;
        private final Credentials mCredentials;

        private TokenCache mTokenCache;

        Builder(RestClient client, Credentials credentials) {
            mClient = client;
            mCredentials = credentials;
        }

        public Builder tokenCache(TokenCache tokenCache) {
            mTokenCache = tokenCache;
            return this;
        }

        public Session create() {
            if (mTokenCache == null) {
                mTokenCache = new InMemoryTokenCache();
            }
            return new Session(mClient, mCredentials, mTokenCache);
        }
    }
}