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

import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.service.RestClient;
import com.jaspersoft.android.sdk.service.Session;
import com.jaspersoft.android.sdk.service.internal.CallExecutor;
import com.jaspersoft.android.sdk.service.internal.DefaultCallExecutor;
import com.jaspersoft.android.sdk.service.internal.InfoCacheManager;
import org.jetbrains.annotations.TestOnly;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryService {
    private final SearchUseCase mSearchUseCase;
    private final InfoCacheManager mInfoCacheManager;

    @TestOnly
    RepositoryService(SearchUseCase searchUseCase, InfoCacheManager infoCacheManager) {
        mSearchUseCase = searchUseCase;
        mInfoCacheManager = infoCacheManager;
    }

    public static RepositoryService create(RestClient client, Session session) {
        RepositoryRestApi repositoryRestApi = new RepositoryRestApi.Builder()
                .baseUrl(client.getServerUrl())
                .connectionTimeOut(client.getConnectionTimeOut(), TimeUnit.MILLISECONDS)
                .readTimeout(client.getReadTimeOut(), TimeUnit.MILLISECONDS)
                .build();
        CallExecutor callExecutor = DefaultCallExecutor.create(client, session);
        InfoCacheManager cacheManager = InfoCacheManager.create(client);

        ResourceMapper resourceMapper = new ResourceMapper();
        SearchUseCase searchUseCase = new SearchUseCase(
                resourceMapper,
                repositoryRestApi,
                cacheManager,
                callExecutor
        );
        return new RepositoryService(searchUseCase, cacheManager);
    }

    public SearchTask search(SearchCriteria criteria) {
        return new SearchTaskImpl(InternalCriteria.from(criteria), mSearchUseCase, mInfoCacheManager);
    }
}
