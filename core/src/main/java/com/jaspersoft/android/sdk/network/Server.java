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
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit.Retrofit;

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
    private final Retrofit.Builder mRetrofitBuilder;

    private Server(String baseUrl, OkHttpClient okHttpClient, Retrofit.Builder retrofitBuilder) {
        mBaseUrl = baseUrl;
        mOkHttpClient = okHttpClient;
        mRetrofitBuilder = retrofitBuilder;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public AnonymousClientBuilder newClient() {
        return new AnonymousClientBuilder(this);
    }

    @NotNull
    public AuthorizedClientBuilder newClient(@Nullable Credentials credentials) {
        Utils.checkNotNull(credentials, "Credentials should not be null");
        return new AuthorizedClientBuilder(this, credentials);
    }

    @NotNull
    public String getBaseUrl() {
        return mBaseUrl;
    }

    Retrofit.Builder newRetrofit() {
        return mRetrofitBuilder;
    }

    OkHttpClient client() {
        return mOkHttpClient;
    }

    public static class Builder {
        private Builder() {
        }

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
            Gson configuredGson = GsonFactory.create();
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(configuredGson);
            StringConverterFactory stringConverterFactory = StringConverterFactory.create();

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(stringConverterFactory)
                    .addConverterFactory(gsonConverterFactory);

            return new Server(mBaseUrl, mOkHttpClient, retrofitBuilder);
        }
    }

    public static class AnonymousClientBuilder {
        private final Server mServer;
        private CookieHandler mCookieHandler =
                new CookieManager(new InMemoryCookieStore(), CookiePolicy.ACCEPT_ORIGINAL_SERVER);

        AnonymousClientBuilder(Server server) {
            mServer = server;
        }

        public AnonymousClientBuilder withCookieHandler(CookieHandler cookieHandler) {
            mCookieHandler = cookieHandler;
            return this;
        }

        public AnonymousClient create() {
            OkHttpClient anonymousClient = mServer.client().clone();
            anonymousClient.setFollowRedirects(false);
            anonymousClient.setCookieHandler(mCookieHandler);

            Retrofit anonymousRetrofit = mServer.newRetrofit()
                    .client(anonymousClient)
                    .build();
            return new AnonymousClient(anonymousRetrofit);
        }
    }

    public static class AuthorizedClientBuilder {
        private final Server mServer;
        private final Credentials mCredentials;

        private AuthPolicy mAuthenticationPolicy;
        private CookieHandler mCookieHandler = new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER);

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
            OkHttpClient authClient = configureAuthClient(mServer.client().clone());
            Retrofit authRetrofit = mServer.newRetrofit()
                    .client(authClient)
                    .build();

            AnonymousClient anonymousClient = mServer.newClient().create();
            return new AuthorizedClient(authRetrofit, anonymousClient);
        }

        private OkHttpClient configureAuthClient(OkHttpClient client) {
            client.setCookieHandler(mCookieHandler);
            AuthStrategy authStrategy = configureAuthStrategy(client);
            configureAuthenticator(client, authStrategy);
            return client;
        }

        private AuthStrategy configureAuthStrategy(OkHttpClient client) {
            OkHttpClient authClient = client.clone();
            authClient.setFollowRedirects(false);
            Retrofit authRetrofit = mServer.newRetrofit()
                    .client(authClient)
                    .build();

            SpringAuthServiceFactory springAuthServiceFactory = new SpringAuthServiceFactory(authRetrofit);
            return new AuthStrategy(springAuthServiceFactory);
        }

        private void configureAuthenticator(OkHttpClient client, AuthStrategy authStrategy) {
            Authenticator recoverableAuthenticator =
                    new RecoverableAuthenticator(authStrategy, mCredentials);

            Authenticator authenticator = recoverableAuthenticator;
            if (mAuthenticationPolicy == AuthPolicy.FAIL_FAST) {
                authenticator = new SingleTimeAuthenticator(recoverableAuthenticator);
            }
            client.setAuthenticator(authenticator);
        }
    }
}
