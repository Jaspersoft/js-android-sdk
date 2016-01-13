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

import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class SearchTaskV5_6Plus extends SearchTask {
    public static final List<Resource> EMPTY_RESPONSE = Collections.emptyList();
    private final static int UNDEFINED = -1;

    private final InternalCriteria mInitialCriteria;
    private final SearchUseCase mSearchUseCase;

    private int mUserOffset;
    private int mInternalOffset = UNDEFINED;
    private boolean mEndReached;

    public SearchTaskV5_6Plus(InternalCriteria criteria, SearchUseCase searchUseCase) {
        mSearchUseCase = searchUseCase;

        // Internally enabling 'forceFullPageFlag'
        mInitialCriteria = criteria.newBuilder()
                .forceFullPage(true)
                .create();
        mUserOffset = criteria.getOffset();
    }

    @NotNull
    @Override
    public List<Resource> nextLookup() throws ServiceException {
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

    @NotNull
    private List<Resource> performLookup() throws ServiceException {
        InternalCriteria newSearchCriteria = createNextCriteria();
        SearchResult result = performApiCall(newSearchCriteria);
        updateInternalOffset(result);
        return result.getResources();
    }

    @NotNull
    private SearchResult performApiCall(InternalCriteria newSearchCriteria) throws ServiceException {
        return mSearchUseCase.performSearch(newSearchCriteria);
    }

    private void defineInternalOffset() throws ServiceException {
        if (mUserOffset == 0) {
            mInternalOffset = mUserOffset;
        } else {
            InternalCriteria newCriteria = mInitialCriteria.newBuilder()
                    .limit(mUserOffset)
                    .offset(0)
                    .create();
            SearchResult result = performApiCall(newCriteria);
            mInternalOffset = result.getNextOffset();
        }
    }

    private void updateInternalOffset(SearchResult result) {
        int nextOffset = result.getNextOffset();

        mEndReached = (nextOffset == 0);
        if (!mEndReached) {
            mInternalOffset = nextOffset;
        }
    }

    @NotNull
    private InternalCriteria createNextCriteria() {
        InternalCriteria.Builder newCriteriaBuilder = mInitialCriteria.newBuilder();
        newCriteriaBuilder.offset(mInternalOffset);
        return newCriteriaBuilder.create();
    }
}
