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
    private final static int UNDEFINED = -1;

    private final RepositoryRestApi.Factory mRepoFactory;
    private final SearchCriteria mInitialCriteria;

    private int mUserOffset;
    private int mInternalOffset = UNDEFINED;
    private boolean mEndReached;

    private Collection<ResourceLookupResponse> tempBuffer = Collections.emptyList();
    private SearchCriteria mNextCriteria;

    public EmeraldMR2SearchStrategy(RepositoryRestApi.Factory repositoryApiFactory, SearchCriteria criteria) {
        mRepoFactory = repositoryApiFactory;
        mInitialCriteria = criteria.newBuilder().create();

        mUserOffset = criteria.getOffset();
    }

    @Override
    public Observable<Collection<ResourceLookupResponse>> searchNext() {
        return Observable.defer(new Func0<Observable<Collection<ResourceLookupResponse>>>() {
            @Override
            public Observable<Collection<ResourceLookupResponse>> call() {
                if (mInternalOffset == UNDEFINED) {
                    defineInternalOffset();
                }
                if (mEndReached){
                    return Observable.just(EMPTY_RESPONSE);
                }
                mNextCriteria = mInitialCriteria.newBuilder()
                        .offset(mInternalOffset)
                        .create();
                return Observable.just(performAlignedRequests());
            }
        });
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }

    private void defineInternalOffset() {
        mInternalOffset = 0;
        if (mUserOffset != 0) {
            mNextCriteria = mInitialCriteria.newBuilder()
                    .limit(mUserOffset)
                    .offset(0)
                    .create();
            performAlignedRequests();
        }
    }

    private Collection<ResourceLookupResponse> performAlignedRequests() {
        int emptyRequestCount = 1;
        ResourceSearchResponse response = performApiCall();

        List<ResourceLookupResponse> collectionFromApi = response.getResources();

        List<ResourceLookupResponse> localBuffer = new LinkedList<>();
        localBuffer.addAll(collectionFromApi);

        Collection<ResourceLookupResponse> result;

        if (tempBuffer.isEmpty()) {
            result = alignBuffer(localBuffer, emptyRequestCount);
        } else {
            List<ResourceLookupResponse> accumulatedBuffer = new LinkedList<>(tempBuffer);
            accumulatedBuffer.addAll(localBuffer);
            result = alignBuffer(accumulatedBuffer, emptyRequestCount);
        }

        return result;
    }

    private Collection<ResourceLookupResponse> alignBuffer(List<ResourceLookupResponse> accumulatedBuffer, int count) {
        int limit = mNextCriteria.getLimit();
        if (accumulatedBuffer.size() > limit) {
            return cacheRemainedBuffer(accumulatedBuffer);
        } else if (accumulatedBuffer.size() < limit) {
            return alignBufferWithResponse(accumulatedBuffer, count);
        } else {
            tempBuffer = Collections.emptyList();
            return accumulatedBuffer;
        }
    }

    @NonNull
    private Collection<ResourceLookupResponse> cacheRemainedBuffer(List<ResourceLookupResponse> buffer) {
        int limit = mNextCriteria.getLimit();
        Collection<ResourceLookupResponse> leftPart = buffer.subList(0, limit - 1);
        Collection<ResourceLookupResponse> rightPart = buffer.subList(limit - 1, buffer.size());
        tempBuffer = rightPart;
        return leftPart;
    }

    private Collection<ResourceLookupResponse> alignBufferWithResponse(List<ResourceLookupResponse> resources, int count) {
        ResourceSearchResponse response = performApiCall();
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

        if (collectionFromApi.isEmpty()) {
            count++;
        }

        return alignBuffer(resources, count);
    }

    private ResourceSearchResponse performApiCall() {
        RepositoryRestApi api = mRepoFactory.get();
        ResourceSearchResponse response = api.searchResources(mNextCriteria.toMap());
        updateNextOffset();
        updateNextCriteria();
        return response;
    }

    private void updateNextOffset() {
        mInternalOffset += mNextCriteria.getLimit();
    }

    private void updateNextCriteria() {
        mNextCriteria = mNextCriteria.newBuilder()
                .offset(mInternalOffset)
                .create();
    }
}
