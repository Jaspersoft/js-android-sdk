package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
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
    @Nullable
    private final Trigger mTrigger;


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
        mTrigger = builder.mTrigger;
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

    @Nullable
    public Trigger getTrigger() {
        return mTrigger;
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
                .withOutputFormats(mOutputFormats)
                .withTrigger(mTrigger);
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
        private Trigger mTrigger;

        /**
         * Allows to specify version of form. One is required for update operations.
         *
         * @param version can be any whole number that represents
         * @return builder for convenient configuration
         */
        public Builder withVersion(@Nullable Integer version) {
            mVersion = version;
            return this;
        }

        /**
         * Allows to specify report that was targeted for job execution.
         *
         * @param jobSource object that encapsulates job source metadata
         * @return builder for convenient configuration
         */
        public Builder withJobSource(@NotNull JobSource jobSource) {
            mJobSource = jobSource;
            return this;
        }

        /**
         * Allows to specify destination, such as output folder where result of schedule will be stored
         *
         * @param repositoryDestination the place where schedule execution artifacts will be stored
         * @return builder for convenient configuration
         */
        public Builder withRepositoryDestination(@NotNull RepositoryDestination repositoryDestination) {
            mRepositoryDestination = repositoryDestination;
            return this;
        }

        /**
         * Allows to specify the name of schedule job
         *
         * @param label name of job
         * @return builder for convenient configuration
         */
        public Builder withLabel(@NotNull String label) {
            Preconditions.checkNotNull(label, "Label should not be null");
            mLabel = label;
            return this;
        }

        /**
         * Allows to specify the description of schedule job
         *
         * @param description of job
         * @return builder for convenient configuration
         */
        public Builder withDescription(@Nullable String description) {
            mDescription = description;
            return this;
        }

        /**
         * Name of base output file
         *
         * @param baseOutputFilename any acceptable name that is restricted by set of invalid file system characters
         * @return builder for convenient configuration
         */
        public Builder withBaseOutputFilename(@NotNull String baseOutputFilename) {
            Preconditions.checkNotNull(baseOutputFilename, "Output file name should not be null");
            mBaseOutputFilename = baseOutputFilename;
            return this;
        }

        /**
         * Allows to specify collection of target output formats
         *
         * @param outputFormats any allowed job schedule formats
         * @return builder for convenient configuration
         */
        public Builder withOutputFormats(@NotNull Collection<JobOutputFormat> outputFormats) {
            Preconditions.checkNotNull(outputFormats, "Formats should not be null");
            mOutputFormats = new HashSet<>(outputFormats);
            return this;
        }

        /**
         * Allows to specify start data of job schedule execution
         *
         * @param startDate any future date. All dates supplied by this field would use default server timezone if one skipped
         * @return builder for convenient configuration
         */
        public Builder withStartDate(@Nullable Date startDate) {
            mStartDate = startDate;
            return this;
        }

        /**
         * Allows to specify respective timezone, so that time conversion issue will be resolved
         *
         * @param timeZone if value not supplied will fallback to JRS current time zone
         * @return builder for convenient configuration
         */
        public Builder withTimeZone(@Nullable TimeZone timeZone) {
            mTimeZone = timeZone;
            return this;
        }

        /**
         * Allows to specify either none, simple or calendar triggers
         * 
         * @param trigger specifies the frequency with which job will be executed
         * @return builder for convenient configuration
         */
        public Builder withTrigger(@Nullable Trigger trigger) {
            mTrigger = trigger;
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
