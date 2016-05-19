package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class JobSearchCriteria {
    public static int UNLIMITED_ROW_NUMBER = Integer.MAX_VALUE;

    @Nullable
    private final String mReportUri;
    @Nullable
    private final JobOwner mOwner;
    @Nullable
    private final String mLabel;
    private final int mOffset;
    private final int mLimit;
    @Nullable
    private final JobSortType mJobSortType;
    @Nullable
    private final Boolean mAscending;

    @TestOnly
    JobSearchCriteria(Builder builder) {
        mReportUri = builder.mReportUri;
        mOwner = builder.mOwner;
        mLabel = builder.mLabel;
        mOffset = builder.mOffset;
        mLimit = builder.mLimit;
        mJobSortType = builder.mJobSortType;
        mAscending = builder.mAscending;
    }

    @Nullable
    public String getReportUri() {
        return mReportUri;
    }

    @Nullable
    public JobOwner getOwner() {
        return mOwner;
    }

    @Nullable
    public String getLabel() {
        return mLabel;
    }

    public int getOffset() {
        return mOffset;
    }

    public int getLimit() {
        return mLimit;
    }

    @Nullable
    public JobSortType getSortType() {
        return mJobSortType;
    }

    @Nullable
    public Boolean getAscending() {
        return mAscending;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobSearchCriteria criteria = (JobSearchCriteria) o;

        if (mLimit != criteria.mLimit) return false;
        if (mOffset != criteria.mOffset) return false;
        if (mAscending != null ? !mAscending.equals(criteria.mAscending) : criteria.mAscending != null) return false;
        if (mJobSortType != criteria.mJobSortType) return false;
        if (mLabel != null ? !mLabel.equals(criteria.mLabel) : criteria.mLabel != null) return false;
        if (mOwner != null ? !mOwner.equals(criteria.mOwner) : criteria.mOwner != null) return false;
        if (mReportUri != null ? !mReportUri.equals(criteria.mReportUri) : criteria.mReportUri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mReportUri != null ? mReportUri.hashCode() : 0;
        result = 31 * result + (mOwner != null ? mOwner.hashCode() : 0);
        result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
        result = 31 * result + mOffset;
        result = 31 * result + mLimit;
        result = 31 * result + (mJobSortType != null ? mJobSortType.hashCode() : 0);
        result = 31 * result + (mAscending != null ? mAscending.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ScheduleSearchParams{" +
                "mReportUri='" + mReportUri + '\'' +
                ", mOwner=" + mOwner +
                ", mLabel='" + mLabel + '\'' +
                ", mOffset=" + mOffset +
                ", mLimit=" + mLimit +
                ", mJobSortType=" + mJobSortType +
                ", mAscending=" + mAscending +
                '}';
    }

    @NotNull
    public static JobSearchCriteria empty() {
        return new Builder().build();
    }

    public static class Builder {
        private String mReportUri;
        private JobOwner mOwner;
        private String mLabel;
        private int mOffset = 0;
        private int mLimit = UNLIMITED_ROW_NUMBER;
        private JobSortType mJobSortType;
        private Boolean mAscending;

        private Builder() {
        }

        private Builder(JobSearchCriteria criteria) {
            mReportUri = criteria.mReportUri;
            mOwner = criteria.mOwner;
            mLabel = criteria.mLabel;
            mOffset = criteria.mOffset;
            mLimit = criteria.mLimit;
            mJobSortType = criteria.mJobSortType;
            mAscending = criteria.mAscending;
        }

        public JobSearchCriteria build() {
            return new JobSearchCriteria(this);
        }

        public Builder withReportUri(@Nullable String reportUri) {
            mReportUri = ifEmptySubstituteStringWithNull(reportUri);
            return this;
        }

        public Builder withOwner(@Nullable JobOwner owner) {
            mOwner = owner;
            return this;
        }

        public Builder withLabel(@Nullable String label) {
            mLabel = ifEmptySubstituteStringWithNull(label);
            return this;
        }

        public Builder withOffset(int startIndex) {
            Preconditions.checkArgument(startIndex >= 0, "Start index must be positive");
            mOffset = startIndex;
            return this;
        }

        public Builder withLimit(int limit) {
            Preconditions.checkArgument(limit > 0, "Row number must be positive");
            mLimit = limit;
            return this;
        }

        public Builder withSortType(@Nullable JobSortType jobSortType) {
            mJobSortType = jobSortType;
            return this;
        }

        public Builder withAscending(@Nullable Boolean ascending) {
            mAscending = ascending;
            return this;
        }

        private String ifEmptySubstituteStringWithNull(String value) {
            boolean isEmpty = (value == null || value.length() == 0);
            return isEmpty ? null : value;
        }
    }
}
