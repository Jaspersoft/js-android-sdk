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
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class SearchTaskImpl implements SearchTask {
    private final InternalCriteria mCriteria;
    private final SearchUseCase mSearchUseCase;
    private final InfoCacheManager mInfoCacheManager;

    @Nullable
    private SearchStrategy strategy;

    @TestOnly
    SearchTaskImpl(InternalCriteria criteria,
                   SearchUseCase searchUseCase,
                   InfoCacheManager infoCacheManager) {
        mCriteria = criteria;
        mSearchUseCase = searchUseCase;
        mInfoCacheManager = infoCacheManager;
    }

    @NotNull
    @Override
    public Collection<Resource> nextLookup() throws ServiceException {
        return defineSearchStrategy().searchNext();
    }

    @Override
    public boolean hasNext() {
        /**
         * Strategy not defined only, if user has not made any lookup requests.
         * There is no 100% guarantee that API has items until we made request.
         */
        if (strategy != null) {
            return strategy.hasNext();
        }
        return true;
    }

    private SearchStrategy defineSearchStrategy() throws ServiceException {
        if (strategy == null) {
            double version = mInfoCacheManager.getInfo().getVersion();

            strategy = SearchStrategy.Factory.get(
                    mSearchUseCase,
                    mCriteria,
                    version
            );
        }
        return strategy;
    }
}
