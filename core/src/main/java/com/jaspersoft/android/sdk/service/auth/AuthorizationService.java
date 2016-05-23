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

package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.*;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * The corresponding service allows to perform authorization related tasks
 *
 * <pre>
 * {@code
 *
 *  Server server = Server.builder()
 *          .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *          .build();
 *
 *  AnonymousClient anonymousClient = server.newClient().create();
 *
 *  Credentials credentials = SpringCredentials.builder()
 *          .withPassword("phoneuser")
 *          .withUsername("phoneuser")
 *          .withOrganization("organization_1")
 *          .build();
 *
 *  AuthorizationService service = AuthorizationService.newService(anonymousClient);
 *  try {
 *      Credentials authorize = service.authorize(credentials);
 *  } catch (ServiceException e) {
 *      // handle API exception
 *  }
 *
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class AuthorizationService {
    private final AnonymousClient mClient;
    private final ServiceExceptionMapper mServiceExceptionMapper;

    @TestOnly
    AuthorizationService(AnonymousClient client,
                         ServiceExceptionMapper mapper) {
        mServiceExceptionMapper = mapper;
        mClient = client;
    }

    /**
     * Performs authorize call on basis of passed credentials
     *
     * @param credentials user sensitive data
     * @return credentials use has supplied if operation was successful
     * @throws ServiceException wraps both http/network/api related errors
     */
    public Credentials authorize(Credentials credentials) throws ServiceException {
        try {
            AuthenticationRestApi authenticationRestApi = mClient.authenticationApi();
            authenticationRestApi.authenticate(credentials);
            return credentials;
        } catch (IOException e) {
            throw mServiceExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mServiceExceptionMapper.transform(e);
        }
    }

    /**
     * Factory method to create new service
     *
     * @param client anonymous network client
     * @return instance of newly created service
     */
    @NotNull
    public static AuthorizationService newService(@NotNull AnonymousClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");
        ServiceExceptionMapper serviceExceptionMapper = DefaultExceptionMapper.getInstance();
        return new AuthorizationService(client, serviceExceptionMapper);
    }
}
