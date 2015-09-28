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
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.FolderLookupResponse;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookupResponse;

import rx.Observable;
import rx.functions.Func0;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryService {
    private final RepositoryRestApi.Factory mRepositoryApiFactory;
    private final ServerRestApi.Factory mInfoApiFactory;

    public RepositoryService(RepositoryRestApi.Factory repositoryApiFactory,
                             ServerRestApi.Factory infoApiFactory) {
        mRepositoryApiFactory = repositoryApiFactory;
        mInfoApiFactory = infoApiFactory;
    }

    public SearchTask search(SearchCriteria criteria) {
        return new SearchTask(criteria, mRepositoryApiFactory, mInfoApiFactory);
    }

    public Observable<FolderLookupResponse> requestFolder(@NonNull final String folderUri) {
        return Observable.defer(new Func0<Observable<FolderLookupResponse>>() {
            @Override
            public Observable<FolderLookupResponse> call() {
                FolderLookupResponse result = mRepositoryApiFactory.get()
                        .requestFolderResource(folderUri);
                return Observable.just(result);
            }
        });
    }

    public Observable<ReportLookupResponse> requestReport(@NonNull final String folderUri) {
        return Observable.defer(new Func0<Observable<ReportLookupResponse>>() {
            @Override
            public Observable<ReportLookupResponse> call() {
                ReportLookupResponse result = mRepositoryApiFactory.get()
                        .requestReportResource(folderUri);
                return Observable.just(result);
            }
        });
    }
}
