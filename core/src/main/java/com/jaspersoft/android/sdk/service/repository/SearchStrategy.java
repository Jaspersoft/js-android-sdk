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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.0
 */
interface SearchStrategy {
    Collection<Resource> searchNext() throws ServiceException;
    boolean hasNext();

    class Factory {
        public static SearchStrategy get(SearchUseCase searchUseCase, InternalCriteria criteria, ServerVersion version) {
            if (version.lessThanOrEquals(ServerVersion.v5_5)) {
                return new EmeraldMR2SearchStrategy(criteria, searchUseCase);
            }
            if (version.greaterThanOrEquals(ServerVersion.v5_6)) {
                return new EmeraldMR3SearchStrategy(criteria, searchUseCase);
            }
            throw new UnsupportedOperationException("Could not resolve searchNext strategy for serverVersion: " + version);
        }
    }
}
