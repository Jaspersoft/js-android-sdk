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
import com.jaspersoft.android.sdk.network.entity.execution.ErrorDescriptor;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportExceptionMapper extends AbstractServiceExceptionMapper {
    private static class SingletonHolder {
        private static final AbstractServiceExceptionMapper INSTANCE = new ReportExceptionMapper();
    }

    /**
     * Initialization-on-demand holder idiom
     *
     * <a href="https://en.wikipedia.org/wiki/Singleton_pattern">SOURCE</a>

     * @return report exception mapper
     */
    public static AbstractServiceExceptionMapper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private final AbstractServiceExceptionMapper mDelegate;

    private ReportExceptionMapper() {
        mDelegate = DefaultExceptionMapper.getInstance();
    }

    @Override
    protected ServiceException mapHttpCodesToState(HttpException e) {
        return mDelegate.mapHttpCodesToState(e);
    }

    @Override
    protected ServiceException mapDescriptorToState(HttpException e, ErrorDescriptor descriptor) {
        ServiceException exception = mDelegate.mapDescriptorToState(e, descriptor);
        if (exception.code() == StatusCodes.RESOURCE_NOT_FOUND) {
            return new ServiceException(exception.getMessage(), exception.getCause(), StatusCodes.REPORT_EXECUTION_INVALID);
        }
        if (descriptor.getErrorCodes().contains("webservices.error.errorExportingReportUnit")) {
            return new ServiceException(exception.getMessage(), exception.getCause(), StatusCodes.EXPORT_EXECUTION_FAILED);
        }
        return exception;
    }
}
