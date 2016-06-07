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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
final class MemoryFilterSearchTask extends JobSearchTask {
    @NotNull
    private final ReportScheduleUseCase mUseCase;

    private List<JobUnit> mAll = Collections.emptyList();
    private List<JobUnit> mBuffer = Collections.emptyList();
    private JobSearchCriteria mCriteria;
    private boolean mEndReached;

    @TestOnly
    MemoryFilterSearchTask(@NotNull ReportScheduleUseCase useCase, @NotNull JobSearchCriteria criteria) {
        mUseCase = useCase;
        mCriteria = criteria;
        mEndReached = false;
    }

    @NotNull
    @Override
    public List<JobUnit> nextLookup() throws ServiceException {
        if (!mEndReached) {
            if (mAll.isEmpty()) {
                mAll = loadAllJobs();
                mAll = filter(mCriteria.getLabel());
            }
            mBuffer = getJobs(mCriteria.getOffset(), mCriteria.getLimit());
            mCriteria = mCriteria.newBuilder()
                    .withOffset(newOffset(mCriteria))
                    .build();
            mEndReached = (mBuffer.size() != mCriteria.getLimit());
        }
        return Collections.unmodifiableList(mBuffer);
    }

    private List<JobUnit> loadAllJobs() throws ServiceException {
        JobSearchCriteria allCriteria = mCriteria.newBuilder()
                .withLabel(null)
                .withLimit(JobSearchCriteria.UNLIMITED_ROW_NUMBER)
                .withOffset(0)
                .build();
        return mUseCase.searchJob(allCriteria);
    }

    private int newOffset(JobSearchCriteria criteria) {
        return criteria.getOffset() + criteria.getLimit();
    }

    private List<JobUnit> filter(String query) {
        String searchQuery = query.toLowerCase();
        List<JobUnit> filteredJobs = new ArrayList<>();
        for (int i = 0; i < mAll.size(); i++) {
            String label = mAll.get(i).getLabel().toLowerCase();
            if (label.contains(searchQuery)) {
                filteredJobs.add(mAll.get(i));
            }
        }
        return filteredJobs;
    }

    private List<JobUnit> getJobs(int offset, int limit) {
        List<JobUnit> selectedJobs = new ArrayList<>();
        int lastIndex = Math.min(mAll.size(), offset + limit);

        for (int i = offset; i < lastIndex; i++) {
            selectedJobs.add(mAll.get(i));
        }
        return selectedJobs;
    }

    @Override
    public boolean hasNext() {
        return !mEndReached;
    }
}
