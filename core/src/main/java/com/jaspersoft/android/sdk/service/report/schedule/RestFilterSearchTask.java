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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class RestFilterSearchTask extends JobSearchTask {
    @NotNull
    private final ReportScheduleUseCase mUseCase;

    private List<JobUnit> mBuffer = Collections.emptyList();
    private JobSearchCriteria mCriteria;
    private boolean mEndReached;

    @TestOnly
    RestFilterSearchTask(@NotNull ReportScheduleUseCase useCase, @NotNull JobSearchCriteria criteria) {
        mUseCase = useCase;
        mCriteria = criteria;
        mEndReached = false;
    }

    @NotNull
    @Override
    public List<JobUnit> nextLookup() throws ServiceException {
        if (!mEndReached) {
            if (!mBuffer.isEmpty()) {
                JobSearchCriteria oldCriteria = mCriteria;
                mCriteria = oldCriteria.newBuilder()
                        .withOffset(newOffset(oldCriteria))
                        .build();
            }
            mBuffer = mUseCase.searchJob(mCriteria);
            mEndReached = (mBuffer.size() != mCriteria.getLimit());
        }
        return Collections.unmodifiableList(mBuffer);
    }

    private int newOffset(JobSearchCriteria criteria) {
        return criteria.getOffset() + criteria.getLimit();
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }
}
