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

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.service.InfoProvider;
import com.jaspersoft.android.sdk.service.auth.TokenProvider;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;

import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class SearchUseCase {
    private final RepositoryRestApi mRestApi;
    private final TokenProvider mTokenProvider;
    private final InfoProvider mInfoProvider;
    private final ResourceMapper mDataMapper;

    public SearchUseCase(
            ResourceMapper dataMapper, RepositoryRestApi restApi,
            TokenProvider tokenProvider, InfoProvider infoProvider) {
        mRestApi = restApi;
        mTokenProvider = tokenProvider;
        mInfoProvider = infoProvider;
        mDataMapper = dataMapper;
    }

    @NonNull
    public SearchResult performSearch(@NonNull InternalCriteria criteria) {
        ResourceSearchResult response = mRestApi.searchResources(mTokenProvider.provideToken(), criteria.toMap());
        SimpleDateFormat dateTimeFormat = mInfoProvider.provideDateTimeFormat();

        SearchResult searchResult = new SearchResult();
        searchResult.setNextOffset(response.getNextOffset());

        Collection<Resource> resources = mDataMapper.transform(response.getResources(), dateTimeFormat);
        searchResult.setResources(resources);

        return searchResult;
    }
}
