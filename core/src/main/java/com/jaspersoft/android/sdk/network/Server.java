/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.OkHttpClient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.CookieHandler;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

/**
 * Represents particular Server configuration.
 * With corresponding API you can configure two types of clients.
 *
 * @author Tom Koptel
 * @see AuthorizedClient
 * @see AnonymousClient
 * @since 2.3
 */
public final class Server {
    private final String mBaseUrl;
    private final OkHttpClient mOkHttpClient;
    private final NetworkClient.Builder mNetworkBuilder;

    private Server(String baseUrl, OkHttpClient okHttpClient, NetworkClient.Builder networkBuilder) {
        mBaseUrl = baseUrl;
        mOkHttpClient = okHttpClient;
        mNetworkBuilder = networkBuilder;
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

    NetworkClient.Builder newNetworkClient() {
        return mNetworkBuilder;
    }

    OkHttpClient client() {
        return mOkHttpClient;
    }

    public static class Builder {
        private String mBaseUrl;
        private final OkHttpClient mOkHttpClient = new OkHttpClient();

        private Builder() {
        }

        /**
         * Sets the address of server to be used while performing REST calls.
         *
         * WARNING: your address of JRS should end by trailing slash.
         * For instance, <a href="http://my.jasper/server-pro/">http://my.jasper/server-pro/</a>
         *
         * @param baseUrl the web address of JRS instance
         * @return builder for convenient configuration
         */
        public Builder withBaseUrl(String baseUrl) {
            mBaseUrl = Utils.checkNotNull(baseUrl, "Base url should not be null");
            return this;
        }

        /**
         * Sets the default connect timeout for new connections. A value of 0 means no timeout, otherwise
         * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
         *
         * @param timeout specifies the duration of timeout
         * @param unit    specifies the type of time out. For instances, could be TimeUnit.MILLISECONDS
         * @return builder for convenient configuration
         * @see java.net.URLConnection#setConnectTimeout(int)
         */
        public Builder withConnectionTimeOut(long timeout, TimeUnit unit) {
            mOkHttpClient.setConnectTimeout(timeout, unit);
            return this;
        }

        /**
         * Sets the default read timeout for new connections. A value of 0 means no timeout, otherwise
         * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
         *
         * @param timeout specifies the duration of timeout
         * @param unit    specifies the type of time out. For instances, could be TimeUnit.MILLISECONDS
         * @return builder for convenient configuration
         * @see java.net.URLConnection#setReadTimeout(int)
         */
        public Builder withReadTimeout(long timeout, TimeUnit unit) {
            mOkHttpClient.setReadTimeout(timeout, unit);
            return this;
        }

        /**
         * Sets the HTTP proxy that will be used by connections created by this
         * client. To disable proxy use completely, call {@code setProxy(Proxy.NO_PROXY)}.
         *
         * @param proxy your proxy
         * @return builder for convenient configuration
         */
        public Builder withProxy(Proxy proxy) {
            mOkHttpClient.setProxy(proxy);
            return this;
        }


        /**
         * Sets the verifier used to confirm that response certificates apply to
         * requested hostnames for HTTPS connections.
         *
         * @param hostnameVerifier your hostname verifier
         * @return builder for convenient configuration
         */
        public Builder withHostnameVerifier(HostnameVerifier hostnameVerifier) {
            mOkHttpClient.setHostnameVerifier(hostnameVerifier);
            return this;
        }

        /**
         *Sets the socket factory used to secure HTTPS connections.
         *If unset, a lazily created SSL socket factory will be used.
         *
         * @param sslSocketFactory your ssl socket factory
         * @return builder for convenient configuration
         */
        public Builder withSslSocketFactory(SSLSocketFactory sslSocketFactory) {
            mOkHttpClient.setSslSocketFactory(sslSocketFactory);
            return this;
        }

        public Server build() {
            Utils.checkNotNull(mBaseUrl, "Server can not be created with null base url");

            NetworkClient.Builder networkBuilder = new NetworkClient.Builder()
                    .setBaseUrl(mBaseUrl)
                    .setGson(GsonFactory.create());

            return new Server(mBaseUrl, mOkHttpClient, networkBuilder);
        }
    }

    public static class AnonymousClientBuilder {
        private final Server mServer;
        private CookieHandler mCookieHandler = CookieHandler.getDefault();

        AnonymousClientBuilder(Server server) {
            mServer = server;
        }

        /**
         * Sets the cookie handler to be used to read outgoing cookies and write
         * incoming cookies.
         *
         * <p>If unset, the {@link CookieHandler#getDefault() system-wide default}</p>
         * cookie handler will be used.
         *
         * @param cookieHandler custom cookie handler
         * @return builder for convenient configuration
         */
        public AnonymousClientBuilder withCookieHandler(CookieHandler cookieHandler) {
            mCookieHandler = cookieHandler;
            return this;
        }

        public AnonymousClient create() {
            OkHttpClient anonymousClient = mServer.client().clone();
            anonymousClient.setFollowRedirects(false);
            anonymousClient.setCookieHandler(mCookieHandler);

            NetworkClient networkClient = mServer.newNetworkClient()
                    .setClient(anonymousClient)
                    .build();
            return new AnonymousClient(networkClient);
        }
    }

    public static class AuthorizedClientBuilder {
        private final Server mServer;
        private final Credentials mCredentials;

        private AuthPolicy mAuthenticationPolicy;
        private AuthenticationLifecycle authenticationLifecycle = AuthenticationLifecycle.NULL;
        private CookieHandler mCookieHandler = CookieHandler.getDefault();

        AuthorizedClientBuilder(Server server, Credentials credentials) {
            mServer = server;
            mCredentials = credentials;
        }

        /**
         * Sets authentication policy to be used while restoring session.
         * By default SDK configured with retry policy.
         *
         * @param authenticationPolicy accepts currently supported auth policy type
         * @return builder for convenient configuration
         * @see AuthPolicy#FAIL_FAST
         * @see AuthPolicy#RETRY
         */
        public AuthorizedClientBuilder withAuthenticationPolicy(AuthPolicy authenticationPolicy) {
            mAuthenticationPolicy = authenticationPolicy;
            return this;
        }

        /**
         * Sets the cookie handler to be used to read outgoing cookies and write
         * incoming cookies.
         *
         * <p>If unset, the {@link CookieHandler#getDefault() system-wide default}</p>
         * cookie handler will be used.
         *
         * @param cookieHandler custom cookie handler
         * @return builder for convenient configuration
         */
        public AuthorizedClientBuilder withCookieHandler(CookieHandler cookieHandler) {
            mCookieHandler = cookieHandler;
            return this;
        }

        /**
         * Sets listener that will be invoked each time client stumbles upon on authentication error
         *
         * @param authenticationLifecycle callback that is called during atuh error
         * @return builder for convenient configuration
         */
        public AuthorizedClientBuilder withAuthenticationLifecycle(@Nullable AuthenticationLifecycle authenticationLifecycle) {
            if (authenticationLifecycle == null) {
                authenticationLifecycle = AuthenticationLifecycle.NULL;
            }
            this.authenticationLifecycle = authenticationLifecycle;
            return this;
        }

        public AuthorizedClient create() {
            OkHttpClient authClient = configureAuthClient(mServer.client().clone());
            NetworkClient networkClient = mServer.newNetworkClient()
                    .setClient(authClient)
                    .build();

            AnonymousClient anonymousClient = mServer.newClient()
                    .withCookieHandler(mCookieHandler)
                    .create();
            return new AuthorizedClient(networkClient, anonymousClient);
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
            NetworkClient networkClient = mServer.newNetworkClient()
                    .setClient(authClient)
                    .build();

            SpringAuthServiceFactory springAuthServiceFactory = new SpringAuthServiceFactory(networkClient);
            PreAuthenticationServiceFactory preAuthenticationServiceFactory = new PreAuthenticationServiceFactory(networkClient);
            SingleSignOnServiceFactory singleSignOnServiceFactory = new SingleSignOnServiceFactory(networkClient);
            return new AuthStrategy(springAuthServiceFactory, preAuthenticationServiceFactory, singleSignOnServiceFactory, authenticationLifecycle);
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
