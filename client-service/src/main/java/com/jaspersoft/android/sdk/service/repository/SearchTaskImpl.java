/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookupResponse;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class SearchTaskImpl implements SearchTask {
    private final InternalCriteria mCriteria;
    private final RepositoryRestApi.Factory mRepositoryApiFactory;
    private final ServerRestApi.Factory mInfoApiFactory;

    @Nullable
    private SearchStrategy strategy;

    SearchTaskImpl(InternalCriteria criteria,
                   RepositoryRestApi.Factory repositoryApiFactory,
                   ServerRestApi.Factory infoApiFactory) {
        mCriteria = criteria;
        mRepositoryApiFactory = repositoryApiFactory;
        mInfoApiFactory = infoApiFactory;
    }

    @NonNull
    @Override
    public Collection<ResourceLookupResponse> nextLookup() {
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

    private SearchStrategy defineSearchStrategy() {
        if (strategy == null) {
            String version = mInfoApiFactory.get().requestVersion();
            strategy = SearchStrategy.Factory.get(version, mRepositoryApiFactory, mCriteria);


        }
        return strategy;
    }
}
