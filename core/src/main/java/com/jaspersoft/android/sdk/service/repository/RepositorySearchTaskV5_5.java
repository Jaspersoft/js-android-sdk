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
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class RepositorySearchTaskV5_5 extends RepositorySearchTask {
    private static final List<Resource> EMPTY_RESPONSE = Collections.emptyList();
    private static final int MAX_RETRY_COUNT = 5;

    private final InternalCriteria mInitialCriteria;
    private final SearchUseCase mSearchUserCase;

    private List<Resource> mBuffer = new LinkedList<>();
    private int mServerDisposition;
    private boolean mEndReached;

    public RepositorySearchTaskV5_5(InternalCriteria criteria, SearchUseCase searchUseCase) {
        mSearchUserCase = searchUseCase;
        mInitialCriteria = criteria;
        mEndReached = false;
    }

    @NotNull
    @Override
    public List<Resource> nextLookup() throws ServiceException {
        int limit = mInitialCriteria.getLimit();
        int offset = mInitialCriteria.getOffset();

        if (mEndReached || limit == 0) {
            return EMPTY_RESPONSE;
        }
        calculateDisposition(offset);
        return Collections.unmodifiableList(internalSearch(limit));
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }

    private void calculateDisposition(int offset) throws ServiceException {
        boolean serverDispositionUndefined = offset >= mServerDisposition;
        if (serverDispositionUndefined) {
            internalSearch(offset);
        }
    }

    @NotNull
    private List<Resource> internalSearch(int limit) throws ServiceException {
        int count = 0;
        while (mBuffer.size() < limit && hasNext()) {
            SearchResult response = performSearch(limit);
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
        List<Resource> result = mBuffer.subList(0, median);
        mBuffer = mBuffer.subList(median, mBuffer.size());
        return result;
    }

    @NotNull
    private SearchResult performSearch(int limit) throws ServiceException {
        InternalCriteria nextCriteria = mInitialCriteria.newBuilder()
                .offset(mServerDisposition)
                .limit(limit)
                .create();
        return mSearchUserCase.performSearch(nextCriteria);
    }
}
