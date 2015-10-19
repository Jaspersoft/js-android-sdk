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

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class EmeraldMR2SearchStrategy implements SearchStrategy {
    private static final Collection<ResourceLookup> EMPTY_RESPONSE = Collections.emptyList();
    private static final int MAX_RETRY_COUNT = 5;

    private final RepositoryRestApi mRepoFactoryRestApi;
    private final InternalCriteria mInitialCriteria;

    private int mServerDisposition;
    private List<ResourceLookup> mBuffer = new LinkedList<>();
    private boolean mEndReached;

    public EmeraldMR2SearchStrategy(RepositoryRestApi repositoryApiFactory, InternalCriteria criteria) {
        mRepoFactoryRestApi = repositoryApiFactory;
        mInitialCriteria = criteria;
        mEndReached = false;
    }

    @Override
    public Collection<ResourceLookup> searchNext() {
        int limit = mInitialCriteria.getLimit();
        int offset = mInitialCriteria.getOffset();

        if (mEndReached || limit == 0) {
            return EMPTY_RESPONSE;
        }
        calculateDisposition(offset);
        return internalSearch(limit);
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }

    private void calculateDisposition(int offset) {
        boolean serverDispositionUndefined = offset >= mServerDisposition;
        if (serverDispositionUndefined) {
            internalSearch(offset);
        }
    }

    @NonNull
    private Collection<ResourceLookup> internalSearch(int limit) {
        int count = 0;
        while (mBuffer.size() < limit && hasNext()) {
            ResourceSearchResult response = performSearch(limit);
            mBuffer.addAll(response.getResources());
            mServerDisposition += limit;

            if (response.getResources().isEmpty()) {
                /**
                 * This is ugly condition. API for 5.5 is broken and there is no way to
                 * determine weather we reached total count value.
                 *
                 * Corresponding situation happens if API has returned 204 for request.
                 * We are trying to retry request 5 times. If 5 times sequentially
                 * received 204 we consider that API has no more items for client to consume
                 */
                mEndReached = (count == MAX_RETRY_COUNT);
                count++;
            }
        }

        int median = Math.min(limit, mBuffer.size());
        Collection<ResourceLookup> result = mBuffer.subList(0, median);
        mBuffer = mBuffer.subList(median, mBuffer.size());
        return result;
    }

    @NonNull
    private ResourceSearchResult performSearch(int limit) {
        InternalCriteria nextCriteria = mInitialCriteria.newBuilder()
                .offset(mServerDisposition)
                .limit(limit)
                .create();
        return mRepoFactoryRestApi.searchResources(nextCriteria.toMap());
    }
}
