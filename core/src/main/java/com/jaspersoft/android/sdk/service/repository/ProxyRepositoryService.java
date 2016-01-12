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

import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ProxyRepositoryService extends RepositoryService {
    private final SearchUseCase mSearchUseCase;
    private final RepositoryUseCase mRepositoryUseCase;
    private final InfoCacheManager mInfoCacheManager;

    @TestOnly
    ProxyRepositoryService(
            SearchUseCase searchUseCase,
            RepositoryUseCase repositoryUseCase,
            InfoCacheManager infoCacheManager) {
        mSearchUseCase = searchUseCase;
        mRepositoryUseCase = repositoryUseCase;
        mInfoCacheManager = infoCacheManager;
    }

    @NotNull
    @Override
    public SearchTask search(@Nullable SearchCriteria criteria) {
        if (criteria == null) {
            criteria = SearchCriteria.none();
        }

        InternalCriteria internalCriteria = InternalCriteria.from(criteria);
        SearchTaskFactory searchTaskFactory = new SearchTaskFactory(internalCriteria, mSearchUseCase, mInfoCacheManager);
        return new SearchTaskProxy(searchTaskFactory);
    }

    @NotNull
    @Override
    public ReportResource fetchReportDetails(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        return mRepositoryUseCase.getReportDetails(reportUri);
    }
}
