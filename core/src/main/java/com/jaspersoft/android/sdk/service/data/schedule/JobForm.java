package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobForm {
    @Nullable
    private final Integer mVersion;
    @NotNull
    private final String mLabel;
    @Nullable
    private final String mDescription;
    @NotNull
    private final String mBaseOutputFilename;
    @NotNull
    private final JobSource mSource;
    @NotNull
    private final RepositoryDestination mRepositoryDestination;
    @NotNull
    private final Set<JobOutputFormat> mOutputFormats;
    @Nullable
    private final Date mStartDate;
    @Nullable
    private final TimeZone mTimeZone;


    JobForm(Builder builder) {
        mVersion = builder.mVersion;
        mLabel = builder.mLabel;
        mDescription = builder.mDescription;
        mBaseOutputFilename = builder.mBaseOutputFilename;
        mSource = builder.mJobSource;
        mRepositoryDestination = builder.mRepositoryDestination;
        mOutputFormats = builder.mOutputFormats;
        mStartDate = builder.mStartDate;
        mTimeZone = builder.mTimeZone;
    }

    @Nullable
    public Integer getVersion() {
        return mVersion;
    }

    @NotNull
    public String getLabel() {
        return mLabel;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @NotNull
    public String getBaseOutputFilename() {
        return mBaseOutputFilename;
    }

    @NotNull
    public JobSource getSource() {
        return mSource;
    }

    @NotNull
    public RepositoryDestination getRepositoryDestination() {
        return mRepositoryDestination;
    }

    @NotNull
    public Set<JobOutputFormat> getOutputFormats() {
        return mOutputFormats;
    }

    @Nullable
    public Date getStartDate() {
        return mStartDate;
    }

    @Nullable
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    @NotNull
    public Builder newBuilder() {
        return new Builder()
                .withVersion(mVersion)
                .withLabel(mLabel)
                .withDescription(mDescription)
                .withBaseOutputFilename(mBaseOutputFilename)
                .withJobSource(mSource)
                .withRepositoryDestination(mRepositoryDestination)
                .withStartDate(mStartDate)
                .withTimeZone(mTimeZone)
                .withDescription(mDescription)
                .withOutputFormats(mOutputFormats);
    }

    public static class Builder {
        private Integer mVersion;
        private String mLabel;
        private String mDescription;
        private String mBaseOutputFilename;
        private RepositoryDestination mRepositoryDestination;
        private JobSource mJobSource;
        private Date mStartDate;
        private TimeZone mTimeZone;

        private Set<JobOutputFormat> mOutputFormats;

        public Builder withVersion(@Nullable Integer version) {
            mVersion = version;
            return this;
        }

        public Builder withJobSource(@NotNull JobSource jobSource) {
            mJobSource = jobSource;
            return this;
        }

        public Builder withRepositoryDestination(@NotNull RepositoryDestination repositoryDestination) {
            mRepositoryDestination = repositoryDestination;
            return this;
        }

        public Builder withLabel(@NotNull String label) {
            Preconditions.checkNotNull(label, "Label should not be null");
            mLabel = label;
            return this;
        }

        public Builder withDescription(@Nullable String description) {
            mDescription = description;
            return this;
        }

        public Builder withBaseOutputFilename(@NotNull String baseOutputFilename) {
            Preconditions.checkNotNull(baseOutputFilename, "Output file name should not be null");
            mBaseOutputFilename = baseOutputFilename;
            return this;
        }

        public Builder withOutputFormats(@NotNull Collection<JobOutputFormat> outputFormats) {
            Preconditions.checkNotNull(outputFormats, "Formats should not be null");
            mOutputFormats = new HashSet<>(outputFormats);
            return this;
        }

        public Builder withStartDate(@Nullable Date startDate) {
            mStartDate = startDate;
            return this;
        }

        public Builder withTimeZone(@Nullable TimeZone timeZone) {
            mTimeZone = timeZone;
            return this;
        }

        public JobForm build() {
            assertState();
            return new JobForm(this);
        }

        private void assertState() {
            if (mOutputFormats.isEmpty()) {
                throw new IllegalStateException("Job can not be scheduled without output format");
            }
            Preconditions.checkNotNull(mLabel, "Job can not be scheduled without label");
            Preconditions.checkNotNull(mJobSource, "Job can not be scheduled without source");
            Preconditions.checkNotNull(mRepositoryDestination, "Job can not be scheduled without repository destination");
            Preconditions.checkNotNull(mBaseOutputFilename, "Job can not be scheduled without output file name");
        }
    }
}
