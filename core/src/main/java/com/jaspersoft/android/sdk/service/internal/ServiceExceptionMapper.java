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
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServiceExceptionMapper {
    @NotNull
    public static ServiceException transform(IOException e) {
        return new ServiceException("Failed to perform network request. Check network!", e, StatusCodes.NETWORK_ERROR);
    }

    @NotNull
    public static ServiceException transform(HttpException e) {
        try {
            ErrorDescriptor descriptor = e.getDescriptor();
            if (e.getDescriptor() == null) {
                return mapHttpCodesToState(e);
            } else {
                return mapDescriptorToState(e, descriptor);
            }
        } catch (IOException ioEx) {
            return transform(ioEx);
        }
    }

    @NotNull
    private static ServiceException mapHttpCodesToState(HttpException e) {
        switch (e.code()) {
            case 500:
                return new ServiceException("Server encountered unexpected error", e, StatusCodes.INTERNAL_ERROR);
            case 404:
                return new ServiceException("Service exist but requested entity not found", e, StatusCodes.CLIENT_ERROR);
            case 403:
                return new ServiceException("User has no access to resource", e, StatusCodes.PERMISSION_DENIED_ERROR);
            case 401:
                return new ServiceException("User is not authorized", e, StatusCodes.AUTHORIZATION_ERROR);
            case 400:
                return new ServiceException("Some parameters in request not valid", e, StatusCodes.CLIENT_ERROR);
            default:
                return new ServiceException("The operation failed with no more detailed information", e, StatusCodes.ERROR);
        }
    }

    @NotNull
    private static ServiceException mapDescriptorToState(HttpException e, ErrorDescriptor descriptor) {
        if ("export.pages.out.of.range".equals(descriptor.getErrorCode())) {
            return new ServiceException(descriptor.getMessage(), e, StatusCodes.EXPORT_PAGE_OUT_OF_RANGE);
        } else {
            return mapHttpCodesToState(e);
        }
    }
}
