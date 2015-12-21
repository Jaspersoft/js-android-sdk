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

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import retrofit.Retrofit;

import java.io.IOException;
import java.net.Proxy;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class AuthorizedAuthClientState implements AuthClientState {

    private Retrofit mRetrofit;
    private Client mClient;

    private ReportExecutionRestApi mReportExecutionRestApi;
    private ReportExportRestApi mReportExportRestApi;
    private ReportOptionRestApi mReportOptionRestApi;
    private InputControlRestApi mInputControlRestApi;
    private RepositoryRestApi mRepositoryRestApi;

    @Override
    public void connect(Client context) throws IOException, HttpException {
        mClient = context;
        makeAuthCall();
    }

    private void makeAuthCall() throws IOException, HttpException {
        Server server = mClient.mAnonymousServer;
        Credentials credentials = mClient.credentials;
        AuthStrategy authStrategy = new AuthStrategy(server);
        credentials.apply(authStrategy);
    }

    @Override
    public ReportExecutionRestApi makeReportExecutionApi() {
        if (mReportExecutionRestApi == null) {
            mReportExecutionRestApi = new ReportExecutionRestApiImpl(getRetrofit());
        }
        return mReportExecutionRestApi;
    }

    @Override
    public ReportExportRestApi makeReportExportRestApi() {
        if (mReportExportRestApi == null) {
            mReportExportRestApi = new ReportExportRestApiImpl(getRetrofit());
        }
        return mReportExportRestApi;
    }

    @Override
    public ReportOptionRestApi makeReportOptionRestApi() {
        if (mReportOptionRestApi == null) {
            mReportOptionRestApi = new ReportOptionRestApiImpl(getRetrofit());
        }
        return mReportOptionRestApi;
    }

    @Override
    public InputControlRestApi makeInputControlRestApi() {
        if (mInputControlRestApi == null) {
            mInputControlRestApi = new InputControlRestApiImpl(getRetrofit());
        }
        return mInputControlRestApi;
    }

    @Override
    public RepositoryRestApi makeRepositoryRestApi() {
        if (mRepositoryRestApi == null) {
            mRepositoryRestApi = new RepositoryRestApiImpl(getRetrofit());
        }
        return mRepositoryRestApi;
    }

    Retrofit getRetrofit() {
        if (mRetrofit == null) {
            RetrofitFactory retrofitFactory = new RetrofitFactory(mClient.mAnonymousServer);
            Retrofit.Builder builder = retrofitFactory.newRetrofit();

            OkHttpClient okHttpClient = configureOkHttp();
            builder.client(okHttpClient);

            mRetrofit = builder.build();
        }

        return mRetrofit;
    }

    private OkHttpClient configureOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        if (mClient.authenticationPolicy == AuthPolicy.RETRY) {
            okHttpClient.setAuthenticator(new Authenticator() {
                @Override
                public Request authenticate(Proxy proxy, Response response) throws IOException {
                    try {
                        makeAuthCall();
                    } catch (HttpException code) {
                        return null;
                    }
                    return null;
                }

                @Override
                public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                    return null;
                }
            });
        }
        return okHttpClient;
    }
}
