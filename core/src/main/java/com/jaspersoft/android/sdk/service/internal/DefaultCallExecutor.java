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

package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class DefaultCallExecutor implements CallExecutor {

    private final TokenCacheManager mTokenCacheManager;
    private final ServiceExceptionMapper mServiceExceptionMapper;

    public DefaultCallExecutor(TokenCacheManager tokenCacheManager, ServiceExceptionMapper serviceExceptionMapper) {
        mTokenCacheManager = tokenCacheManager;
        mServiceExceptionMapper = serviceExceptionMapper;
    }

    @NotNull
    public <T> T execute(Call<T> call) throws ServiceException {
        try {
            String token = mTokenCacheManager.loadToken();
            return call.perform(token);
        } catch (IOException e) {
            throw mServiceExceptionMapper.transform(e);
        } catch (HttpException e) {
            if (e.code() == 401) {
                mTokenCacheManager.invalidateToken();

                try {
                    String token = mTokenCacheManager.loadToken();
                    return call.perform(token);
                } catch (IOException e1) {
                    throw mServiceExceptionMapper.transform(e1);
                } catch (HttpException e1) {
                    throw mServiceExceptionMapper.transform(e1);
                }
            } else {
                throw mServiceExceptionMapper.transform(e);
            }
        }
    }
}
