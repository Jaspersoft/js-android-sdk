/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
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

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class EmeraldMR2SearchStrategy implements SearchStrategy {
    private final RepositoryRestApi.Factory mRepoFactory;
    private final SearchCriteria mInitialCriteria;
    private final int mLimit;

    private int mNextOffset;
    private boolean mFirstCall;

    public EmeraldMR2SearchStrategy(RepositoryRestApi.Factory repositoryApiFactory, SearchCriteria criteria) {
        mRepoFactory = repositoryApiFactory;
        mInitialCriteria = criteria.newBuilder().create();

        mNextOffset = criteria.getOffset();
        mLimit = criteria.getLimit();

        mFirstCall = true;
    }

    @Override
    public Observable<Collection<ResourceLookupResponse>> search() {
        return Observable.defer(new Func0<Observable<Collection<ResourceLookupResponse>>>() {
            @Override
            public Observable<Collection<ResourceLookupResponse>> call() {
                return Observable.just(performAlignedRequests());
            }
        });
    }

    private Collection<ResourceLookupResponse> performAlignedRequests() {
        ResourceSearchResponse response = makeCall();

        List<ResourceLookupResponse> collectionFromApi = response.getResources();
        if (collectionFromApi.isEmpty()) {
            return collectionFromApi;
        }

        Collection<ResourceLookupResponse> buffer = new LinkedList<>();
        buffer.addAll(collectionFromApi);

        if (mLimit == 0) {
            return buffer;
        } else {
            if (buffer.size() == mLimit) {
                return buffer;
            } else {
                return alignResponse(buffer);
            }
        }
    }

    private Collection<ResourceLookupResponse> alignResponse(Collection<ResourceLookupResponse> buffer) {
        ResourceSearchResponse response = makeCall();
        List<ResourceLookupResponse> collectionFromApi = response.getResources();
        if (collectionFromApi.isEmpty()) {
            return buffer;
        }

        buffer.addAll(collectionFromApi);
        int actual = buffer.size();

        if (actual < mLimit) {
            return alignResponse(buffer);
        }
        if (actual > mLimit) {
            List<ResourceLookupResponse> resources = new ArrayList<>(buffer);
            Collection<ResourceLookupResponse> aligned = resources.subList(0, mLimit - 1);
            mNextOffset -= (actual - mLimit);
            return aligned;
        }
        return buffer;
    }

    private ResourceSearchResponse makeCall() {
        SearchCriteria newSearchCriteria = resolveNextCriteria();
        RepositoryRestApi api = mRepoFactory.get();
        return api.searchResources(newSearchCriteria.toMap());
    }

    private void updateNextOffset() {
        if (!mFirstCall) {
            mNextOffset += mLimit;
        }
        mFirstCall = false;
    }

    private SearchCriteria resolveNextCriteria() {
        updateNextOffset();

        SearchCriteria.Builder newCriteriaBuilder = mInitialCriteria.newBuilder();
        newCriteriaBuilder.offset(mNextOffset);

        return newCriteriaBuilder.create();
    }
}
