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

/**
 * The client that encapsulates all REST API objects that does not require JRS active session.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class AnonymousClient extends Client {
    private ServerRestApi mServerRestApi;
    private AuthenticationRestApi mAuthApi;

    AnonymousClient(NetworkClient networkClient) {
        super(networkClient);
    }

    /**
     * Provides instance of particular info API.
     *
     * @return api that connects info related calls
     */
    public ServerRestApi infoApi() {
        if (mServerRestApi == null) {
            mServerRestApi = new ServerRestApi(mNetworkClient);
        }
        return mServerRestApi;
    }

    /**
     * Provides instance of particular authentication API.
     *
     * @return api that verifies correctness of credentials
     */
    public AuthenticationRestApi authenticationApi() {
        if (mAuthApi == null) {
            SpringAuthServiceFactory authServiceFactory = new SpringAuthServiceFactory(mNetworkClient);
            AuthStrategy authStrategy = new AuthStrategy(authServiceFactory);
            mAuthApi = new AuthenticationRestApi(authStrategy);
        }
        return mAuthApi;
    }
}
