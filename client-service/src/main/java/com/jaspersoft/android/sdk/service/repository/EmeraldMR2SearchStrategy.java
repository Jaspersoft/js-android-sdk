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
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class EmeraldMR2SearchStrategy implements SearchStrategy {
    private static final Collection<ResourceLookupResponse> EMPTY_RESPONSE = Collections.emptyList();
    private static final int RETRY_COUNT = 5;

    private final RepositoryRestApi.Factory mRepoFactory;
    private final SearchCriteria mInitialCriteria;
    private final int mLimit;

    private int mNextOffset;
    private boolean mEndReached;
    private Collection<ResourceLookupResponse> tempBuffer = Collections.emptyList();

    public EmeraldMR2SearchStrategy(RepositoryRestApi.Factory repositoryApiFactory, SearchCriteria criteria) {
        mRepoFactory = repositoryApiFactory;
        mInitialCriteria = criteria.newBuilder().create();

        mNextOffset = criteria.getOffset();
        mLimit = criteria.getLimit();
    }

    @Override
    public Observable<Collection<ResourceLookupResponse>> searchNext() {
        return Observable.defer(new Func0<Observable<Collection<ResourceLookupResponse>>>() {
            @Override
            public Observable<Collection<ResourceLookupResponse>> call() {
                if (mEndReached){
                    return Observable.just(EMPTY_RESPONSE);
                }
                return Observable.just(performAlignedRequests());
            }
        });
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }

    private Collection<ResourceLookupResponse> performAlignedRequests() {
        SearchCriteria newSearchCriteria = createNextCriteria();
        ResourceSearchResponse response = performApiCall(newSearchCriteria);

        List<ResourceLookupResponse> collectionFromApi = response.getResources();

        List<ResourceLookupResponse> localBuffer = new LinkedList<>();
        localBuffer.addAll(collectionFromApi);

        Collection<ResourceLookupResponse> result;
        int count = 0;
        if (tempBuffer.isEmpty()) {
            result = alignBuffer(localBuffer, count);
        } else {
            List<ResourceLookupResponse> accumulatedBuffer = new LinkedList<>(tempBuffer);
            accumulatedBuffer.addAll(localBuffer);
            result = alignBuffer(accumulatedBuffer, count);
        }

        return result;
    }

    private Collection<ResourceLookupResponse> alignBuffer(List<ResourceLookupResponse> accumulatedBuffer, int count) {
        if (accumulatedBuffer.size() > mLimit) {
            return cacheRemainedBuffer(accumulatedBuffer);
        } else if (accumulatedBuffer.size() < mLimit) {
            return alignBufferWithResponse(accumulatedBuffer, count);
        } else {
            return accumulatedBuffer;
        }
    }

    @NonNull
    private Collection<ResourceLookupResponse> cacheRemainedBuffer(List<ResourceLookupResponse> buffer) {
        Collection<ResourceLookupResponse> leftPart = buffer.subList(0, mLimit - 1);
        Collection<ResourceLookupResponse> rightPart = buffer.subList(mLimit - 1, buffer.size());
        tempBuffer = rightPart;
        return leftPart;
    }

    private Collection<ResourceLookupResponse> alignBufferWithResponse(List<ResourceLookupResponse> resources, int count) {
        count++;

        SearchCriteria newSearchCriteria = createNextCriteria();
        ResourceSearchResponse response = performApiCall(newSearchCriteria);
        List<ResourceLookupResponse> collectionFromApi = response.getResources();
        resources.addAll(collectionFromApi);

        /**
         * This is ugly condition. API for 5.5 is broken and there is no way to
         * determine weather we reached total count value.
         *
         * Corresponding situation happens if API has returned 204 for request.
         * We are trying to retry request 5 times. If 5 times sequentially
         * received 204 we consider that API has no more items for client to consume
         */
        mEndReached = (count == RETRY_COUNT);
        if (mEndReached) {
            return resources;
        }

        return alignBuffer(resources, count);
    }

    private SearchCriteria createNextCriteria() {
        SearchCriteria.Builder newCriteriaBuilder = mInitialCriteria.newBuilder();
        newCriteriaBuilder.offset(mNextOffset);

        return newCriteriaBuilder.create();
    }

    private ResourceSearchResponse performApiCall(SearchCriteria criteria) {
        RepositoryRestApi api = mRepoFactory.get();
        ResourceSearchResponse response = api.searchResources(criteria.toMap());
        updateNextOffset();
        return response;
    }

    private void updateNextOffset() {
        mNextOffset += mLimit;
    }
}
