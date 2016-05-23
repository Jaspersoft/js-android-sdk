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

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobData {
    @NotNull
    private final int mId;
    private final int mVersion;
    @NotNull
    private final String mUsername;
    @NotNull
    private final String mLabel;
    @NotNull
    private final String mDescription;
    @NotNull
    private final Date mCreationDate;
    @NotNull
    private final List<JobOutputFormat> mOutputFormats;

    public JobData(int id,
                   int version,
                   @NotNull String username,
                   @NotNull String label,
                   @NotNull String description,
                   @NotNull Date creationDate,
                   @NotNull List<JobOutputFormat> outputFormats) {
        mId = id;
        mVersion = version;
        mUsername = username;
        mLabel = label;
        mDescription = description;
        mCreationDate = creationDate;
        mOutputFormats = outputFormats;
    }

    @NotNull
    public int getId() {
        return mId;
    }

    public int getVersion() {
        return mVersion;
    }

    @NotNull
    public String getUsername() {
        return mUsername;
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
    public Date getCreationDate() {
        return mCreationDate;
    }

    @NotNull
    public List<JobOutputFormat> getOutputFormats() {
        return mOutputFormats;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobData)) return false;

        JobData jobData = (JobData) o;

        if (mId != jobData.mId) return false;
        if (mVersion != jobData.mVersion) return false;
        if (!mCreationDate.equals(jobData.mCreationDate)) return false;
        if (!mDescription.equals(jobData.mDescription)) return false;
        if (!mLabel.equals(jobData.mLabel)) return false;
        if (!mOutputFormats.equals(jobData.mOutputFormats)) return false;
        if (!mUsername.equals(jobData.mUsername)) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = mId;
        result = 31 * result + mVersion;
        result = 31 * result + mUsername.hashCode();
        result = 31 * result + mLabel.hashCode();
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mCreationDate.hashCode();
        result = 31 * result + mOutputFormats.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "JobData{" +
                "mId='" + mId + '\'' +
                ", mVersion=" + mVersion +
                ", mUsername='" + mUsername + '\'' +
                ", mLabel='" + mLabel + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mCreationDate=" + mCreationDate +
                ", mOutputFormats=" + mOutputFormats +
                '}';
    }

    public static class Builder {
        private int mId;
        private int mVersion;
        private String mUsername;
        private String mLabel;
        private String mDescription;
        private Date mCreationDate;
        private List<JobOutputFormat> mOutputFormats;

        public Builder withId(int id) {
            mId = id;
            return this;
        }

        public Builder withVersion(int version) {
            mVersion = version;
            return this;
        }

        public Builder withUsername(@NotNull String username) {
            mUsername = username;
            return this;
        }

        public Builder withLabel(@NotNull String label) {
            mLabel = label;
            return this;
        }

        public Builder withDescription(@NotNull String description) {
            mDescription = description;
            return this;
        }

        public Builder withCreationDate(@NotNull Date creationDate) {
            mCreationDate = creationDate;
            return this;
        }

        public Builder withOutputFormats(@NotNull List<JobOutputFormat> outputFormats) {
            mOutputFormats = outputFormats;
            return this;
        }

        public JobData build() {
            return new JobData(mId, mVersion, mUsername, mLabel, mDescription, mCreationDate, mOutputFormats);
        }
    }
}
