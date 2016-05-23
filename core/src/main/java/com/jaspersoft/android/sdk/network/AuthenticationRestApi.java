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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 *  Following module delegates authorization call to JRS instance.
 *  As soon as, module abstracted it receives abstract Credentials model that represents a specific type of Authorization.
 *  At the moment, we are supporting only Spring Security mechanism.
 *
 * <pre>
 * {@code
 *
 *    Server server = Server.builder()
 *            .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *            .build();
 *
 *    AnonymousClient anonymousClient = server.newClient()
 *            .create();
 *    AuthenticationRestApi authenticationRestApi = anonymousClient.authenticationApi();
 *
 *    Credentials credentials = SpringCredentials.builder()
 *            .withPassword("phoneuser")
 *            .withUsername("phoneuser")
 *            .withOrganization("organization_1")
 *            .build();
 *    try {
 *        authenticationRestApi.authenticate(credentials);
 *    } catch (IOException e) {
 *        // handle socket issue
 *    } catch (HttpException e) {
 *        // handle network issue
 *    }
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class AuthenticationRestApi {
    private final AuthStrategy mAuthStrategy;

    AuthenticationRestApi(AuthStrategy authStrategy) {
        mAuthStrategy = authStrategy;
    }

    /**
     * Performs authentication on the basis of passed credentials. See {@link SpringCredentials}
     *
     * @param credentials abstract model that wraps sensitive data required for authentication
     * @throws IOException if socket was closed abruptly due to network issues
     * @throws HttpException if rest service encountered any status code above 300
     */
    public void authenticate(@NotNull Credentials credentials) throws IOException, HttpException {
        Utils.checkNotNull(credentials, "Credentials should not be null");
        credentials.apply(mAuthStrategy);
    }
}
