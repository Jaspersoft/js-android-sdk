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

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;
import com.squareup.okhttp.*;
import retrofit.Retrofit;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Server {
    private final String mBaseUrl;
    private final OkHttpClient mOkHttpClient;

    private Server(String baseUrl, OkHttpClient okHttpClient) {
        mBaseUrl = baseUrl;
        mOkHttpClient = okHttpClient;
    }

    public AnonymousClient newClient() {
        return new AnonymousClientImpl(
                newRetrofit()
                        .client(mOkHttpClient)
                        .build()
        );
    }

    public AuthorizedClientBuilder newClient(Credentials credentials) {
        return new AuthorizedClientBuilder(this, credentials);
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    Retrofit.Builder newRetrofit() {
        Gson configuredGson = GsonFactory.create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(configuredGson);
        StringConverterFactory stringConverterFactory = StringConverterFactory.create();

        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(stringConverterFactory)
                .addConverterFactory(gsonConverterFactory);
    }

    OkHttpClient getClient() {
        return mOkHttpClient;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Builder() {}

        public OptionalBuilder withBaseUrl(String baseUrl) {
            return new OptionalBuilder(baseUrl);
        }
    }

    public static class OptionalBuilder {
        private final String mBaseUrl;
        private final OkHttpClient mOkHttpClient = new OkHttpClient();

        private OptionalBuilder(String baseUrl) {
            mBaseUrl = baseUrl;
        }

        public OptionalBuilder withConnectionTimeOut(long timeout, TimeUnit unit) {
            mOkHttpClient.setConnectTimeout(timeout, unit);
            return this;
        }

        public OptionalBuilder withReadTimeout(long timeout, TimeUnit unit) {
            mOkHttpClient.setReadTimeout(timeout, unit);
            return this;
        }

        public OptionalBuilder withProxy(Proxy proxy) {
            mOkHttpClient.setProxy(proxy);
            return this;
        }

        public Server build() {
            return new Server(mBaseUrl, mOkHttpClient);
        }
    }

    public static class AuthorizedClientBuilder {
        private final Server mServer;
        private final Credentials mCredentials;

        private AuthPolicy mAuthenticationPolicy;
        private CookieHandler mCookieHandler;

        AuthorizedClientBuilder(Server server, Credentials credentials) {
            mServer = server;
            mCredentials = credentials;
        }

        public AuthorizedClientBuilder withAuthenticationPolicy(AuthPolicy authenticationPolicy) {
            mAuthenticationPolicy = authenticationPolicy;
            return this;
        }

        public AuthorizedClientBuilder withCookieHandler(CookieHandler cookieHandler) {
            mCookieHandler = cookieHandler;
            return this;
        }

        public AuthorizedClient create() {
            ensureSaneDefaults();

            OkHttpClient authClient = configureAuthClient(mServer.getClient().clone());
            OkHttpClient anonymClient = configureAnonymClient(mServer.getClient().clone());

            Retrofit authRetrofit = mServer.newRetrofit()
                    .client(authClient)
                    .build();
            Retrofit anonymRetrofit = mServer.newRetrofit()
                    .client(anonymClient)
                    .build();

            AnonymousClient anonymousClient = new AnonymousClientImpl(anonymRetrofit);
            return new AuthorizedClientImpl(authRetrofit, anonymousClient);
        }

        private OkHttpClient configureAnonymClient(OkHttpClient client) {
            client.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            return client;
        }

        private OkHttpClient configureAuthClient(OkHttpClient client) {
            client.setCookieHandler(mCookieHandler);

            if (mAuthenticationPolicy == AuthPolicy.FAIL_FAST) {
                client.interceptors().add(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Headers headers = chain.request().headers();
                        boolean hasCookies = headers.names().contains("Cookie");
                        if (!hasCookies) {
                            Response response401 = new Response.Builder()
                                    .protocol(Protocol.HTTP_1_1)
                                    .request(chain.request())
                                    .headers(chain.request().headers())
                                    .code(401)
                                    .build();
                            return response401;
                        }
                        return chain.proceed(chain.request());
                    }
                });

                RecoverableAuthenticator recoverableAuthenticator = new RecoverableAuthenticator(mServer, mCredentials);
                SingleTimeAuthenticator singleTimeAuthenticator = new SingleTimeAuthenticator(recoverableAuthenticator);
                client.setAuthenticator(singleTimeAuthenticator);
            } else {
                RecoverableAuthenticator recoverableAuthenticator = new RecoverableAuthenticator(mServer, mCredentials);
                client.setAuthenticator(recoverableAuthenticator);
            }

            return client;
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
