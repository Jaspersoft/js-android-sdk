/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.rx.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.schedule.JobSearchTask;
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
public class RxJobSearchTask {
    private final JobSearchTask mSyncDelegate;

    @TestOnly
    RxJobSearchTask(JobSearchTask syncDelegate) {
        mSyncDelegate = syncDelegate;
    }

    /**
     * Provides list of jobs on the basis of search criteria
     *
     * @return list of jobs
     */
    @NotNull
    public Observable<List<JobUnit>> nextLookup() {
        return Observable.defer(new Func0<Observable<List<JobUnit>>>() {
            @Override
            public Observable<List<JobUnit>> call() {
                try {
                    List<JobUnit> jobUnits = mSyncDelegate.nextLookup();
                    return Observable.just(jobUnits);
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

    /**
     * Provides synchronous counterpart of service
     *
     * @return wrapped version of service {@link JobSearchTask}
     */
    public JobSearchTask toBlocking() {
        return mSyncDelegate;
    }
}
