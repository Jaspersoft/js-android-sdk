/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.*;
import com.jaspersoft.android.sdk.service.internal.info.InMemoryInfoCache;
import com.jaspersoft.android.sdk.service.internal.info.InfoCache;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RepositoryService {
    private final SearchUseCase mSearchUseCase;
    private final RepositoryUseCase mRepositoryUseCase;
    private final InfoCacheManager mInfoCacheManager;

    @TestOnly
    RepositoryService(
            SearchUseCase searchUseCase,
            RepositoryUseCase repositoryUseCase,
            InfoCacheManager infoCacheManager) {
        mSearchUseCase = searchUseCase;
        mRepositoryUseCase = repositoryUseCase;
        mInfoCacheManager = infoCacheManager;
    }

    @NotNull
    public RepositorySearchTask search(@Nullable RepositorySearchCriteria criteria) {
        if (criteria == null) {
            criteria = RepositorySearchCriteria.empty();
        }

        InternalCriteria internalCriteria = InternalCriteria.from(criteria);
        SearchTaskFactory searchTaskFactory = new SearchTaskFactory(internalCriteria, mSearchUseCase, mInfoCacheManager);
        return new RepositorySearchTaskProxy(searchTaskFactory);
    }

    @NotNull
    public Resource fetchResourceDetails(@NotNull String resourceUri, @NotNull ResourceType type) throws ServiceException {
        Preconditions.checkNotNull(resourceUri, "Resource uri should not be null");
        Preconditions.checkNotNull(type, "Resource type should not be null");
        return mRepositoryUseCase.getResourceByType(resourceUri, type);
    }

    @NotNull
    public ResourceOutput fetchResourceContent(@NotNull String resourceUri) throws ServiceException {
        Preconditions.checkNotNull(resourceUri, "Resource uri should not be null");
        return mRepositoryUseCase.getResourceContent(resourceUri);
    }

    @NotNull
    public List<Resource> fetchRootFolders() throws ServiceException {
        Resource rootFolder = fetchResourceDetails("/", ResourceType.folder);
        Resource publicFolder = fetchResourceDetails("/public", ResourceType.folder);
        return Arrays.asList(rootFolder, publicFolder);
    }

    @NotNull
    public static RepositoryService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        InfoCache cache = new InMemoryInfoCache();
        RepositoryRestApi repositoryRestApi = client.repositoryApi();
        ServiceExceptionMapper defaultExMapper = DefaultExceptionMapper.getInstance();
        CallExecutor callExecutor = new DefaultCallExecutor(defaultExMapper);
        InfoCacheManager cacheManager = InfoCacheManager.create(client, cache);

        ResourcesMapper resourceMapper = new ResourcesMapper();
        SearchUseCase searchUseCase = new SearchUseCase(
                resourceMapper,
                repositoryRestApi,
                cacheManager,
                callExecutor
        );
        ResourcesMapper resourcesMapper = new ResourcesMapper();
        RepositoryUseCase repositoryUseCase = new RepositoryUseCase(defaultExMapper,
                repositoryRestApi, resourcesMapper, cacheManager);
        return new RepositoryService(searchUseCase, repositoryUseCase, cacheManager);
    }
}
