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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class SearchTaskFactory {
    private final InternalCriteria mInternalCriteria;
    private final SearchUseCase mSearchUseCase;
    private final InfoCacheManager mCacheManager;

    SearchTaskFactory(InternalCriteria internalCriteria, SearchUseCase searchUseCase, InfoCacheManager cacheManager) {
        mInternalCriteria = internalCriteria;
        mSearchUseCase = searchUseCase;
        mCacheManager = cacheManager;
    }

    public RepositorySearchTask create() throws ServiceException {
        ServerInfo serverInfo = mCacheManager.getInfo();
        ServerVersion version = serverInfo.getVersion();
        if (version.lessThanOrEquals(ServerVersion.v5_5)) {
            return new RepositorySearchTaskV5_5(mInternalCriteria, mSearchUseCase);
        }
        return new RepositorySearchTaskV5_6Plus(mInternalCriteria, mSearchUseCase);
    }
}
