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

import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.repository.RepositorySearchTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.List;

/**
 * Wraps search results as iterator object.
 * All responses wrapped as Rx {@link rx.Observable}.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxRepositorySearchTask {
    private final RepositorySearchTask mSyncDelegate;

    @TestOnly
    RxRepositorySearchTask(RepositorySearchTask repositorySearchTask) {
        mSyncDelegate = repositorySearchTask;
    }

    /**
     * Provides list of resources on the basis of search criteria
     *
     * @return list of jobs
     */
    @NotNull
    public Observable<List<Resource>> nextLookup() {
        return Observable.defer(new Func0<Observable<List<Resource>>>() {
            @Override
            public Observable<List<Resource>> call() {
                try {
                    List<Resource> result = mSyncDelegate.nextLookup();
                    return Observable.just(result);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Provides flag whether task reached end or not
     *
     * @return true if API able to perform new lookup
     */
    public boolean hasNext() {
        return mSyncDelegate.hasNext();
    }
}
