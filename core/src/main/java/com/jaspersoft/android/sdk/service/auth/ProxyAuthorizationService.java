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

package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthorizationClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ProxyAuthorizationService extends AuthorizationService {
    private final AuthorizationClient mClient;
    private final ServiceExceptionMapper mServiceExceptionMapper;

    @TestOnly
    ProxyAuthorizationService(AuthorizationClient client,
                              ServiceExceptionMapper mapper) {
        mServiceExceptionMapper = mapper;
        mClient = client;
    }

    @Override
    public void authorize(Credentials credentials) throws ServiceException {
        try {
            mClient.authorize(credentials);
        } catch (IOException e) {
            throw mServiceExceptionMapper.transform(e);
        } catch (HttpException e) {
            throw mServiceExceptionMapper.transform(e);
        }
    }
}
