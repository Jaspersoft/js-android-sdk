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
final class LegacyJobSearchTask extends JobSearchTask {
    @NotNull
    private final ReportScheduleUseCase mUseCase;

    private List<JobUnit> mAll = Collections.emptyList();
    private List<JobUnit> mBuffer = Collections.emptyList();
    private JobSearchCriteria mCriteria;
    private boolean mEndReached;

    @TestOnly
    LegacyJobSearchTask(@NotNull ReportScheduleUseCase useCase, @NotNull JobSearchCriteria criteria) {
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
