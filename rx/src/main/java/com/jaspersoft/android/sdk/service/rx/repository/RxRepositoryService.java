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

package com.jaspersoft.android.sdk.service.rx.repository;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.repository.RepositoryService;
import com.jaspersoft.android.sdk.service.repository.SearchCriteria;
import com.jaspersoft.android.sdk.service.repository.SearchTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.List;


/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RxRepositoryService {
    private final RepositoryService mSyncDelegate;

    @TestOnly
    RxRepositoryService(RepositoryService repositoryService) {
        mSyncDelegate = repositoryService;
    }

    @NotNull
    public RxSearchTask search(@Nullable SearchCriteria criteria) {
        SearchTask searchTask = mSyncDelegate.search(criteria);
        return new RxSearchTask(searchTask);
    }

    @NotNull
    public Observable<ReportResource> fetchReportDetails(@NotNull final String reportUri) {
        return Observable.defer(new Func0<Observable<ReportResource>>() {
            @Override
            public Observable<ReportResource> call() {
                try {
                    ReportResource reportResource = mSyncDelegate.fetchReportDetails(reportUri);
                    return Observable.just(reportResource);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public Observable<List<Resource>> fetchRootFolders() {
        return Observable.defer(new Func0<Observable<List<Resource>>>() {
            @Override
            public Observable<List<Resource>> call() {
                try {
                    List<Resource> resources = mSyncDelegate.fetchRootFolders();
                    return Observable.just(resources);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public static RxRepositoryService newService(@NotNull AuthorizedClient authorizedClient) {
        Preconditions.checkNotNull(authorizedClient, "Client should not be null");
        RepositoryService repositoryService = RepositoryService.newService(authorizedClient);
        return new RxRepositoryService(repositoryService);
    }
}
