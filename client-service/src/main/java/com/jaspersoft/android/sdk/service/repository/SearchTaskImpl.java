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

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

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
    public Observable<Collection<ResourceLookupResponse>> nextLookup() {
        return defineSearchStrategy().flatMap(new Func1<SearchStrategy, Observable<Collection<ResourceLookupResponse>>>() {
            @Override
            public Observable<Collection<ResourceLookupResponse>> call(SearchStrategy searchStrategy) {
                return searchStrategy.searchNext();
            }
        });
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

    private Observable<SearchStrategy> defineSearchStrategy() {
        if (strategy == null) {
            return requestServerVersion().flatMap(new Func1<String, Observable<SearchStrategy>>() {
                @Override
                public Observable<SearchStrategy> call(String version) {
                    strategy = SearchStrategy.Factory.get(version, mRepositoryApiFactory, mCriteria);
                    return Observable.just(strategy);
                }
            });
        }
        return Observable.just(strategy);
    }

    private Observable<String> requestServerVersion() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                String version = mInfoApiFactory.get().requestVersion();
                return Observable.just(version);
            }
        });
    }
}
