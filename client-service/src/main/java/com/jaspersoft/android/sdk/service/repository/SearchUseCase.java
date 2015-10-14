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
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class SearchUseCase {
    private final RepositoryRestApi mRestApi;
    private final SearchResultMapper mDataMapper;

    public SearchUseCase(RepositoryRestApi restApi, SearchResultMapper dataMapper) {
        mRestApi = restApi;
        mDataMapper = dataMapper;
    }

    @NonNull
    public SearchResult performSearch(@NonNull InternalCriteria criteria,
                                      @NonNull SimpleDateFormat dateTimeFormat) {
        ResourceSearchResult response = mRestApi.searchResources(criteria.toMap());
        return mDataMapper.transform(response, dateTimeFormat);
    }
}
