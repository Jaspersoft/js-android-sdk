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

import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Client {
    private final Server mServer;
    private final Credentials mCredentials;
    private final AuthPolicy mAuthPolicy;
    private final CookieHandler mCookieHandler;

    private AuthClientState mAuthClientState;

    @TestOnly
    Client(Server server,
           Credentials credentials,
           AuthPolicy authPolicy,
           CookieHandler cookieHandler,
           AuthClientState authClientState) {
        mServer = server;
        mCredentials = credentials;
        mAuthPolicy = authPolicy;
        mCookieHandler = cookieHandler;
        mAuthClientState = authClientState;
    }

    public void connect() throws IOException, HttpException {
        mAuthClientState.connect(this);
    }

    public ReportExecutionRestApi reportExecutionApi() {
        return mAuthClientState.makeReportExecutionApi();
    }

    public ReportExportRestApi reportExportApi() {
        return mAuthClientState.makeReportExportRestApi();
    }

    public ReportOptionRestApi reportOptionsApi() {
        return mAuthClientState.makeReportOptionRestApi();
    }

    public InputControlRestApi inputControlApi() {
        return mAuthClientState.makeInputControlRestApi();
    }

    public RepositoryRestApi repositoryApi() {
        return mAuthClientState.makeRepositoryRestApi();
    }

    public Server getServer() {
        return mServer;
    }

    public Credentials getCredentials() {
        return mCredentials;
    }

    public AuthPolicy getAuthPolicy() {
        return mAuthPolicy;
    }

    public CookieHandler getCookieHandler() {
        return mCookieHandler;
    }

    void setAuthClientState(AuthClientState authClientState) {
        mAuthClientState = authClientState;
    }

    public static class Builder {
        private final Server mServer;
        private final Credentials mCredentials;

        private AuthPolicy mAuthenticationPolicy;
        private CookieHandler mCookieHandler;

        Builder(Server server, Credentials credentials) {
            mServer = server;
            mCredentials = credentials;
        }

        public Builder withAuthenticationPolicy(AuthPolicy authenticationPolicy) {
            mAuthenticationPolicy = authenticationPolicy;
            return this;
        }

        public Builder withCookieHandler(CookieHandler cookieHandler) {
            mCookieHandler = cookieHandler;
            return this;
        }

        public Client create() {
            ensureSaneDefaults();
            AuthClientState state = new InitialAuthClientState();
            return new Client(mServer, mCredentials, mAuthenticationPolicy, mCookieHandler, state);
        }

        private void ensureSaneDefaults() {
            if (mAuthenticationPolicy == null) {
                mAuthenticationPolicy = AuthPolicy.RETRY;
            }
            if (mCookieHandler == null) {
                mCookieHandler = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
            }
        }
    }
}
