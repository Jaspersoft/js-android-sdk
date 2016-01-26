package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class JobSource {
    private final String mUri;
    private final List<ReportParameter> mParameters;

    JobSource(String uri, List<ReportParameter> parameters) {
        mUri = uri;
        mParameters = parameters;
    }

    @NotNull
    public String getUri() {
        return mUri;
    }

    @NotNull
    public List<ReportParameter> getParameters() {
        return mParameters;
    }

    public static class Builder extends NestedBuilder<JobForm.Builder, JobSource> {
        private final JobForm.Builder mParentBuilder;
        private String mUri;
        private List<ReportParameter> mParameters = Collections.emptyList();

        Builder(JobForm.Builder parentBuilder) {
            mParentBuilder = parentBuilder;
            mParameters = new ArrayList<>();
        }

        public Builder withUri(@NotNull String uri) {
            mUri = Preconditions.checkNotNull(uri, "Source uri should not be null");
            return this;
        }

        public Builder withParameters(@Nullable List<ReportParameter> parameters) {
            if (parameters != null) {
                mParameters = Collections.unmodifiableList(parameters);
            }
            return this;
        }

        @Override
        public JobForm.Builder done() {
            return mParentBuilder;
        }

        @Override
        public JobSource build() {
            return new JobSource(mUri, mParameters);
        }

        void assertState() {
            Preconditions.checkNotNull(mUri, "Job can not be scheduled without source uri");
        }
    }
}
