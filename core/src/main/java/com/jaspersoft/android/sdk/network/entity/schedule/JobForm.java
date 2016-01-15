package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobForm {
    @Expose
    private final String label;

    @Expose
    private final String description;

    @Expose
    private final String baseOutputFilename;

    @Expose
    private final JobSource source;

    @Expose
    private final RepositoryDestination repositoryDestination;

    @Expose
    private final JobOutputFormats outputFormats;

    @Expose
    private final JobTrigger trigger;

    JobForm(String label,
            String description,
            String baseOutputFilename,
            JobSource source,
            RepositoryDestination repositoryDestination,
            JobOutputFormats outputFormats,
            JobTrigger trigger) {
        this.label = label;
        this.description = description;
        this.baseOutputFilename = baseOutputFilename;
        this.source = source;
        this.repositoryDestination = repositoryDestination;
        this.outputFormats = outputFormats;
        this.trigger = trigger;
    }

    public String getBaseOutputFilename() {
        return baseOutputFilename;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobForm)) return false;

        JobForm that = (JobForm) o;

        if (baseOutputFilename != null ? !baseOutputFilename.equals(that.baseOutputFilename) : that.baseOutputFilename != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (outputFormats != null ? !outputFormats.equals(that.outputFormats) : that.outputFormats != null)
            return false;
        if (repositoryDestination != null ? !repositoryDestination.equals(that.repositoryDestination) : that.repositoryDestination != null)
            return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (trigger != null ? !trigger.equals(that.trigger) : that.trigger != null) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (baseOutputFilename != null ? baseOutputFilename.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (repositoryDestination != null ? repositoryDestination.hashCode() : 0);
        result = 31 * result + (outputFormats != null ? outputFormats.hashCode() : 0);
        result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
        return result;
    }

    public static class Builder {

        private String mLabel;
        private String mDescription;
        private String mBaseOutputFilename;
        private JobSource mSource;
        private RepositoryDestination mRepositoryDestination;
        private JobTrigger mTrigger;

        private final JobOutputFormats mJobOutputFormats;
        private final List<String> outputFormats;

        public Builder() {
            outputFormats = new ArrayList<>();
            mJobOutputFormats = new JobOutputFormats(outputFormats);
        }

        public Builder withLabel(String label) {
            mLabel = label;
            return this;
        }

        public Builder withDescription(String description) {
            mDescription = description;
            return this;
        }

        public Builder withBaseOutputFilename(String baseOutputFilename) {
            mBaseOutputFilename = baseOutputFilename;
            return this;
        }

        public Builder withSource(String reportUri) {
            mSource = new JobSource(reportUri);
            return this;
        }

        public Builder withRepositoryDestination(String folderUri) {
            mRepositoryDestination = new RepositoryDestination(folderUri);
            return this;
        }

        public Builder addOutputFormat(String format) {
            outputFormats.add(format);
            return this;
        }

        public Builder withSimpleTrigger(JobSimpleTrigger trigger) {
            mTrigger = new JobTrigger(trigger);
            return this;
        }

        public JobForm build() {
            return new JobForm(mLabel,
                    mDescription,
                    mBaseOutputFilename,
                    mSource,
                    mRepositoryDestination,
                    mJobOutputFormats,
                    mTrigger);
        }
    }
}
