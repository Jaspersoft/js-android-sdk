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

import java.util.Collection;

import rx.Observable;
import rx.functions.Func0;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class EmeraldMR3SearchStrategy implements SearchStrategy {
    private final RepositoryRestApi.Factory mRepoFactory;
    private final SearchCriteria mInitialCriteria;

    private int mNextOffset;

    public EmeraldMR3SearchStrategy(RepositoryRestApi.Factory repositoryApiFactory, SearchCriteria criteria) {
        mRepoFactory = repositoryApiFactory;
        // Internally enabling 'forceFullPageFlag'
        mInitialCriteria = criteria.newBuilder()
                .forceFullPage(true)
                .create();

        int initialOffset = (criteria.getOffset() == null) ? 0 : criteria.getOffset();
        mNextOffset = initialOffset;
    }

    @Override
    public Observable<Collection<ResourceLookupResponse>> search() {
        return Observable.defer(new Func0<Observable<Collection<ResourceLookupResponse>>>() {
            @Override
            public Observable<Collection<ResourceLookupResponse>> call() {
                return Observable.just(makeApiCall());
            }
        });
    }

    private Collection<ResourceLookupResponse> makeApiCall() {
        SearchCriteria newSearchCriteria = resolveNextCriteria();
        RepositoryRestApi api = mRepoFactory.get();
        ResourceSearchResponse result = api.searchResources(newSearchCriteria.toMap());
        updateNextOffset(result);
        return result.getResources();
    }

    private void updateNextOffset(ResourceSearchResponse result) {
        int nextOffset = result.getNextOffset();

        boolean endReached = (nextOffset == 0);
        if (!endReached) {
            mNextOffset = nextOffset;
        }
    }

    private SearchCriteria resolveNextCriteria() {
        SearchCriteria.Builder newCriteriaBuilder = mInitialCriteria.newBuilder();
        newCriteriaBuilder.offset(mNextOffset);
        return newCriteriaBuilder.create();
    }
}
