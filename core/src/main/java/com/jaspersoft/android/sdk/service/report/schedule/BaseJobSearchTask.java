package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobUnit;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class BaseJobSearchTask extends JobSearchTask {
    @NotNull
    private final ReportScheduleUseCase mUseCase;

    private List<JobUnit> mBuffer = Collections.emptyList();
    private JobSearchCriteria mCriteria;
    private boolean mEndReached;

    @TestOnly
    BaseJobSearchTask(@NotNull ReportScheduleUseCase useCase, @NotNull JobSearchCriteria criteria) {
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
