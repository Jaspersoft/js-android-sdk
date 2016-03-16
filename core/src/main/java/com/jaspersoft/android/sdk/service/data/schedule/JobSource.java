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
 * @since 2.3
 */
public class JobSource {
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

    public static class Builder {
        private String mUri;
        private List<ReportParameter> mParameters = Collections.emptyList();

        public Builder() {
            mParameters = new ArrayList<>();
        }

        /**
         * Allows to specify resource uri that will be used to schedule job
         *
         * @param uri unique identifier of resources in JRS domain
         * @return builder for convenient configuration
         */
        public Builder withUri(@NotNull String uri) {
            mUri = Preconditions.checkNotNull(uri, "Source uri should not be null");
            return this;
        }

        /**
         * Allows to specify report params that specify exact data requested by user
         *
         * @param parameters list of key/value pair where key corresponds to control state id and value represented by set of values
         * @return builder for convenient configuration
         */
        public Builder withParameters(@Nullable List<ReportParameter> parameters) {
            if (parameters != null) {
                mParameters = Collections.unmodifiableList(parameters);
            }
            return this;
        }

        public JobSource build() {
            assertState();
            return new JobSource(mUri, mParameters);
        }

        private void assertState() {
            Preconditions.checkNotNull(mUri, "Source can not be created without uri");
        }
    }
}
