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

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResponse;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.functions.Func0;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class EmeraldMR2SearchStrategy_ implements SearchStrategy {
    public static final int MAX_RETRY_COUNT = 5;
    private final RepositoryRestApi.Factory mRepoFactory;
    private final SearchCriteria mInitialCriteria;

    private int mServerDisposition;
    private List<ResourceLookupResponse> mBuffer = new LinkedList<>();
    private boolean mEndReached;

    public EmeraldMR2SearchStrategy_(RepositoryRestApi.Factory repositoryApiFactory, SearchCriteria criteria) {
        mRepoFactory = repositoryApiFactory;
        mInitialCriteria = criteria.newBuilder().create();
        mEndReached = false;
    }

    @Override
    public Observable<Collection<ResourceLookupResponse>> searchNext() {
        return Observable.defer(new Func0<Observable<Collection<ResourceLookupResponse>>>() {
            @Override
            public Observable<Collection<ResourceLookupResponse>> call() {
                return Observable.just(internalSearch());
            }
        });
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }

    private Collection<ResourceLookupResponse> internalSearch() {
        int limit = mInitialCriteria.getLimit();

        int count = 0;
        while (mBuffer.size() < limit && hasNext()) {
            ResourceSearchResponse response = performSearch();
            mBuffer.addAll(response.getResources());
            mServerDisposition += limit;

            if (response.getResources().isEmpty()) {
                mEndReached = (count == MAX_RETRY_COUNT);
                count++;
            }
        }

        int measure = Math.min(limit, mBuffer.size());
        Collection<ResourceLookupResponse> result = mBuffer.subList(0, measure);
        mBuffer = mBuffer.subList(measure, mBuffer.size());
        return result;
    }

    private ResourceSearchResponse performSearch() {
        SearchCriteria nextCriteria = mInitialCriteria.newBuilder()
                .offset(mServerDisposition)
                .create();
        RepositoryRestApi api = mRepoFactory.get();
        return api.searchResources(nextCriteria.toMap());
    }
}
