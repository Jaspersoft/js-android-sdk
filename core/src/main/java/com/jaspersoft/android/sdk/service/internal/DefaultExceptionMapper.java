/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class DefaultExceptionMapper extends AbstractServiceExceptionMapper {
    private static class SingletonHolder {
        private static final AbstractServiceExceptionMapper INSTANCE = new DefaultExceptionMapper();
    }

    /**
     * Initialization-on-demand holder idiom
     *
     * <a href="https://en.wikipedia.org/wiki/Singleton_pattern">SOURCE</a>

     * @return default mapper
     */
    public static AbstractServiceExceptionMapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private DefaultExceptionMapper() {
    }

    @NotNull
    protected ServiceException mapDescriptorToState(HttpException e, ErrorDescriptor descriptor) {
        if (descriptor.getErrorCodes().contains("resource.not.found")) {
            return new ServiceException(descriptor.getMessage(), e, StatusCodes.RESOURCE_NOT_FOUND);
        }
        return mapHttpCodesToState(e);
    }

    @NotNull
    protected ServiceException mapHttpCodesToState(HttpException e) {
        int code = e.code();
        if (code >= 500) {
            return new ServiceException("Server encountered unexpected error", e, StatusCodes.INTERNAL_ERROR);
        } else {
            ServiceException undefinedError = new ServiceException("The operation failed with no more detailed information", e, StatusCodes.UNDEFINED_ERROR);
            if (code >= 400 && code < 500) {
                switch (code) {
                    case 404:
                        return new ServiceException("Service exist but requested entity not found", e, StatusCodes.CLIENT_ERROR);
                    case 403:
                        return new ServiceException("User has no access to resource", e, StatusCodes.PERMISSION_DENIED_ERROR);
                    case 401:
                        return new ServiceException("User is not authorized", e, StatusCodes.AUTHORIZATION_ERROR);
                    case 400:
                        return new ServiceException("Some parameters in request not valid", e, StatusCodes.CLIENT_ERROR);
                    default:
                        return undefinedError;
                }
            } else {
                return undefinedError;
            }
        }
    }
}
