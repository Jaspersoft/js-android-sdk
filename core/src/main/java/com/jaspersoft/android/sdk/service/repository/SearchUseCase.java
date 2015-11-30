/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;
import com.jaspersoft.android.sdk.service.call.Call;
import com.jaspersoft.android.sdk.service.call.CallExecutor;
import com.jaspersoft.android.sdk.service.info.InfoCacheManager;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class SearchUseCase {
    private final ResourceMapper mDataMapper;
    private final RepositoryRestApi mRestApi;
    private final InfoCacheManager mInfoCacheManager;
    private final CallExecutor mCallExecutor;

    SearchUseCase(ResourceMapper dataMapper,
                  RepositoryRestApi restApi,
                  InfoCacheManager infoCacheManager,
                  CallExecutor callExecutor) {
        mDataMapper = dataMapper;
        mRestApi = restApi;
        mInfoCacheManager = infoCacheManager;
        mCallExecutor = callExecutor;
    }

    @NotNull
    public SearchResult performSearch(@NotNull final InternalCriteria internalCriteria) throws ServiceException {
        Call<SearchResult> call = new Call<SearchResult>() {
            @Override
            public SearchResult perform(String token) throws IOException, HttpException {
                Map<String, Object> criteria = CriteriaMapper.map(internalCriteria);
                ResourceSearchResult response = mRestApi.searchResources(token, criteria);

                SimpleDateFormat dateTimeFormat = null;
                try {
                    dateTimeFormat = mInfoCacheManager.getInfo().getDatetimeFormatPattern();

                    SearchResult searchResult = new SearchResult();
                    searchResult.setNextOffset(response.getNextOffset());

                    Collection<Resource> resources = mDataMapper.transform(response.getResources(), dateTimeFormat);
                    searchResult.setResources(resources);

                    return searchResult;
                } catch (ServiceException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    if (cause instanceof HttpException) {
                        throw (HttpException) cause;
                    }
                    throw new RuntimeException(cause);
                }
            }
        };
        return mCallExecutor.execute(call);
    }
}
