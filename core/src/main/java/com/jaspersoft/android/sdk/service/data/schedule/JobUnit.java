package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobUnit {
    private final int mId;
    private final int mVersion;
    @NotNull
    private final String mReportUri;
    @NotNull
    private final String mLabel;
    @NotNull
    private final String mDescription;
    @NotNull
    private final JobOwner mOwner;
    @NotNull
    private final JobState mState;
    @Nullable
    private final Date mPreviousFireTime;
    @Nullable
    private final Date mNextFireTime;

    private JobUnit(Builder builder) {
        mId = builder.mId;
        mVersion = builder.mVersion;
        mReportUri = builder.mReportUri;
        mLabel = builder.mLabel;
        mDescription = builder.mDescription;
        mOwner = builder.mOwner;
        mState = builder.mState;
        mPreviousFireTime = builder.mPreviousFireTime;
        mNextFireTime = builder.mNextFireTime;
    }

    public int getId() {
        return mId;
    }

    public int getVersion() {
        return mVersion;
    }

    @NotNull
    public String getReportUri() {
        return mReportUri;
    }

    @NotNull
    public String getLabel() {
        return mLabel;
    }

    @NotNull
    public String getDescription() {
        return mDescription;
    }

    @NotNull
    public JobOwner getOwner() {
        return mOwner;
    }

    @NotNull
    public JobState getState() {
        return mState;
    }

    @Nullable
    public Date getPreviousFireTime() {
        return mPreviousFireTime;
    }

    @Nullable
    public Date getNextFireTime() {
        return mNextFireTime;
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobUnit)) return false;

        JobUnit jobUnit = (JobUnit) o;

        if (mId != jobUnit.mId) return false;
        if (mVersion != jobUnit.mVersion) return false;
        if (!mDescription.equals(jobUnit.mDescription)) return false;
        if (!mLabel.equals(jobUnit.mLabel)) return false;
        if (!mNextFireTime.equals(jobUnit.mNextFireTime)) return false;
        if (!mOwner.equals(jobUnit.mOwner)) return false;
        if (mPreviousFireTime != null ? !mPreviousFireTime.equals(jobUnit.mPreviousFireTime) : jobUnit.mPreviousFireTime != null)
            return false;
        if (!mReportUri.equals(jobUnit.mReportUri)) return false;
        if (mState != jobUnit.mState) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = mId;
        result = 31 * result + mVersion;
        result = 31 * result + mReportUri.hashCode();
        result = 31 * result + mLabel.hashCode();
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mOwner.hashCode();
        result = 31 * result + mState.hashCode();
        result = 31 * result + (mPreviousFireTime != null ? mPreviousFireTime.hashCode() : 0);
        result = 31 * result + mNextFireTime.hashCode();
        return result;
    }

    public static class Builder {
        private int mId;
        private int mVersion;
        private String mReportUri;
        private String mLabel;
        private String mDescription;
        private JobOwner mOwner;
        private JobState mState;
        private Date mPreviousFireTime;
        private Date mNextFireTime;

        public Builder withId(int id) {
            mId = id;
            return this;
        }

        public Builder withVersion(int version) {
            mVersion = version;
            return this;
        }

        public Builder withReportUri(@NotNull String reportUri) {
            mReportUri = Preconditions.checkNotNull(reportUri, "Report uri should not be null");
            return this;
        }

        public Builder withLabel(@NotNull String label) {
            mLabel = Preconditions.checkNotNull(label, "Label should not be null");
            return this;
        }

        public Builder withDescription(@Nullable String description) {
            mDescription = description;
            return this;
        }

        public Builder withOwner(@NotNull JobOwner owner) {
            mOwner = Preconditions.checkNotNull(owner, "Owner should not be null");
            return this;
        }

        public Builder withState(@NotNull JobState state) {
            mState = Preconditions.checkNotNull(state, "State should not be null");
            return this;
        }

        public Builder withPreviousFireTime(@Nullable Date previousFireTime) {
            mPreviousFireTime = previousFireTime;
            return this;
        }

        public Builder withNextFireTime(@Nullable Date nextFireTime) {
            mNextFireTime = nextFireTime;
            return this;
        }

        public JobUnit build() {
            if (mDescription == null) {
                mDescription = "";
            }
            Preconditions.checkNotNull(mReportUri, "Job unit should contain report uri");
            Preconditions.checkNotNull(mLabel, "Job unit should contain label");
            Preconditions.checkNotNull(mOwner, "Job unit should contain owner");
            Preconditions.checkNotNull(mState, "Job unit should contain state");
            return new JobUnit(this);
        }
    }
}
