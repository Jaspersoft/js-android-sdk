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
import com.jaspersoft.android.sdk.service.auth.TokenProvider;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class EmeraldMR3SearchStrategy implements SearchStrategy {
    public static final Collection<ResourceLookup> EMPTY_RESPONSE = Collections.emptyList();
    private final static int UNDEFINED = -1;

    private final RepositoryRestApi mRepositoryRestApi;
    private final InternalCriteria mInitialCriteria;
    private final TokenProvider mTokenProvider;

    private int mUserOffset;
    private int mInternalOffset = UNDEFINED;
    private boolean mEndReached;

    public EmeraldMR3SearchStrategy(InternalCriteria criteria,
                                    RepositoryRestApi repositoryApiFactory,
                                    TokenProvider tokenProvider) {
        mRepositoryRestApi = repositoryApiFactory;
        mTokenProvider = tokenProvider;
        // Internally enabling 'forceFullPageFlag'
        mInitialCriteria = criteria.newBuilder()
                .forceFullPage(true)
                .create();
        mUserOffset = criteria.getOffset();
    }

    @Override
    public Collection<ResourceLookup> searchNext() {
        if (mEndReached || mInitialCriteria.getLimit() == 0){
            return EMPTY_RESPONSE;
        }
        if (mInternalOffset == UNDEFINED) {
            defineInternalOffset();
        }

        return performLookup();
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }

    @NonNull
    private Collection<ResourceLookup> performLookup() {
        InternalCriteria newSearchCriteria = createNextCriteria();
        ResourceSearchResult result = performApiCall(newSearchCriteria);
        updateInternalOffset(result);
        return result.getResources();
    }

    @NonNull
    private ResourceSearchResult performApiCall(InternalCriteria newSearchCriteria) {
        return mRepositoryRestApi.searchResources(newSearchCriteria.toMap(), mTokenProvider.provideToken().get());
    }

    private void defineInternalOffset() {
        if (mUserOffset == 0) {
            mInternalOffset = mUserOffset;
        } else {
            InternalCriteria newCriteria = mInitialCriteria.newBuilder()
                    .limit(mUserOffset)
                    .offset(0)
                    .create();
            ResourceSearchResult result = performApiCall(newCriteria);
            mInternalOffset = result.getNextOffset();
        }
    }

    private void updateInternalOffset(ResourceSearchResult result) {
        int nextOffset = result.getNextOffset();

        mEndReached = (nextOffset == 0);
        if (!mEndReached) {
            mInternalOffset = nextOffset;
        }
    }

    @NonNull
    private InternalCriteria createNextCriteria() {
        InternalCriteria.Builder newCriteriaBuilder = mInitialCriteria.newBuilder();
        newCriteriaBuilder.offset(mInternalOffset);
        return newCriteriaBuilder.create();
    }
}
