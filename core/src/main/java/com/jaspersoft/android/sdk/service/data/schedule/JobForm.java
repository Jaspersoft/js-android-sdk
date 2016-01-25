package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobForm {
    @NotNull
    private final String mLabel;
    @Nullable
    private final String mDescription;
    @NotNull
    private final String mBaseOutputFilename;
    @NotNull
    private final String mSource;
    @NotNull
    private final String mRepositoryDestination;
    @NotNull
    private final Set<JobOutputFormat> mOutputFormats;
    @NotNull
    private final JobTrigger mTrigger;

    JobForm(Builder builder) {
        mLabel = builder.mLabel;
        mDescription = builder.mDescription;
        mBaseOutputFilename = builder.mBaseOutputFilename;
        mSource = builder.mSource;
        mRepositoryDestination = builder.mRepositoryDestination;
        mOutputFormats = builder.mOutputFormats;
        mTrigger = builder.mTrigger;
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
    public String getSource() {
        return mSource;
    }

    @NotNull
    public String getRepositoryDestination() {
        return mRepositoryDestination;
    }

    @NotNull
    public Set<JobOutputFormat> getOutputFormats() {
        return mOutputFormats;
    }

    @NotNull
    public JobTrigger getTrigger() {
        return mTrigger;
    }

    public static class Builder {
        private String mLabel;
        private String mDescription;
        private String mBaseOutputFilename;
        private String mSource;
        private String mRepositoryDestination;
        private JobTrigger mTrigger;
        private final Set<JobOutputFormat> mOutputFormats = new HashSet<>(15);

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

        public Builder withSource(@Nullable String source) {
            Preconditions.checkNotNull(source, "Source should not be null");
            mSource = source;
            return this;
        }

        public Builder withRepositoryDestination(@Nullable String repositoryDestination) {
            Preconditions.checkNotNull(repositoryDestination, "Repository destination should not be null");
            mRepositoryDestination = repositoryDestination;
            return this;
        }

        public Builder addOutputFormats(@NotNull Collection<JobOutputFormat> outputFormats) {
            Preconditions.checkNotNull(outputFormats, "Formats should not be null");
            mOutputFormats.addAll(Collections.unmodifiableCollection(outputFormats));
            return this;
        }

        public Builder addOutputFormat(@NotNull JobOutputFormat outputFormat) {
            Preconditions.checkNotNull(outputFormat, "Format should not be null");
            mOutputFormats.add(outputFormat);
            return this;
        }

        public Builder withTrigger(@NotNull JobTrigger trigger) {
            Preconditions.checkNotNull(trigger, "Trigger should not be null");
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
            Preconditions.checkNotNull(mTrigger, "Job can not be scheduled without trigger");
            Preconditions.checkNotNull(mBaseOutputFilename, "Job can not be scheduled without output file name");
            Preconditions.checkNotNull(mSource, "Job can not be scheduled without source");
            Preconditions.checkNotNull(mRepositoryDestination, "Job can not be scheduled without repo destination");
        }
    }
}
