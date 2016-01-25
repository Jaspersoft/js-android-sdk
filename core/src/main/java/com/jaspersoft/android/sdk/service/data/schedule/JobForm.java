package com.jaspersoft.android.sdk.service.data.schedule;

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
    @Nullable
    private final String mBaseOutputFilename;
    @Nullable
    private final String mSource;
    @Nullable
    private final String mRepositoryDestination;
    @NotNull
    private final Set<JobOutputFormat> mOutputFormats;
    @Nullable
    private final JobTrigger mTrigger;

    @NotNull
    public String getLabel() {
        return mLabel;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @Nullable
    public String getBaseOutputFilename() {
        return mBaseOutputFilename;
    }

    @Nullable
    public String getSource() {
        return mSource;
    }

    @Nullable
    public String getRepositoryDestination() {
        return mRepositoryDestination;
    }

    @NotNull
    public Set<JobOutputFormat> getOutputFormats() {
        return mOutputFormats;
    }

    @Nullable
    public JobTrigger getTrigger() {
        return mTrigger;
    }

    public JobForm(Builder builder) {
        mLabel = builder.mLabel;
        mDescription = builder.mDescription;
        mBaseOutputFilename = builder.mBaseOutputFilename;
        mSource = builder.mSource;
        mRepositoryDestination = builder.mRepositoryDestination;
        mOutputFormats = builder.mOutputFormats;
        mTrigger = builder.mTrigger;
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
            mLabel = label;
            return this;
        }

        public Builder withDescription(@Nullable String description) {
            mDescription = description;
            return this;
        }

        public Builder withBaseOutputFilename(@Nullable String baseOutputFilename) {
            mBaseOutputFilename = baseOutputFilename;
            return this;
        }

        public Builder withSource(@Nullable String source) {
            mSource = source;
            return this;
        }

        public Builder withRepositoryDestination(@Nullable String repositoryDestination) {
            mRepositoryDestination = repositoryDestination;
            return this;
        }

        public Builder addOutputFormats(@NotNull Collection<JobOutputFormat> outputFormats) {
            mOutputFormats.addAll(Collections.unmodifiableCollection(outputFormats));
            return this;
        }

        public Builder addOutputFormat(@NotNull JobOutputFormat outputFormat) {
            mOutputFormats.add(outputFormat);
            return this;
        }

        public Builder withTrigger(@Nullable JobTrigger trigger) {
            mTrigger = trigger;
            return this;
        }

        public JobForm build() {
            return new JobForm(this);
        }
    }
}
