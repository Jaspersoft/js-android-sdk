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
    private final String mReportLabel;
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
        mReportLabel = builder.mReportLabel;
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
    public String getReportLabel() {
        return mReportLabel;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobUnit jobUnit = (JobUnit) o;

        if (mId != jobUnit.mId) return false;
        if (mVersion != jobUnit.mVersion) return false;
        if (!mReportUri.equals(jobUnit.mReportUri)) return false;
        if (!mLabel.equals(jobUnit.mLabel)) return false;
        if (!mReportLabel.equals(jobUnit.mReportLabel)) return false;
        if (!mDescription.equals(jobUnit.mDescription)) return false;
        if (!mOwner.equals(jobUnit.mOwner)) return false;
        if (mState != jobUnit.mState) return false;
        if (mPreviousFireTime != null ? !mPreviousFireTime.equals(jobUnit.mPreviousFireTime) : jobUnit.mPreviousFireTime != null)
            return false;
        return !(mNextFireTime != null ? !mNextFireTime.equals(jobUnit.mNextFireTime) : jobUnit.mNextFireTime != null);

    }

    @Override
    public int hashCode() {
        int result = mId;
        result = 31 * result + mVersion;
        result = 31 * result + mReportUri.hashCode();
        result = 31 * result + mLabel.hashCode();
        result = 31 * result + mReportLabel.hashCode();
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mOwner.hashCode();
        result = 31 * result + mState.hashCode();
        result = 31 * result + (mPreviousFireTime != null ? mPreviousFireTime.hashCode() : 0);
        result = 31 * result + (mNextFireTime != null ? mNextFireTime.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private int mId;
        private int mVersion;
        private String mReportUri;
        private String mReportLabel;
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

        public Builder withReportLabel(@Nullable String reportLabel) {
            mReportLabel = reportLabel;
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
