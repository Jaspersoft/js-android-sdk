package com.jaspersoft.android.sdk.widget.report;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class RunOptions {
    private final String reportUri;
    private final List<ReportParameter> parameters;

    RunOptions(String reportUri, List<ReportParameter> parameters) {
        this.reportUri = reportUri;
        this.parameters = parameters;
    }

    public String getReportUri() {
        return reportUri;
    }

    public List<ReportParameter> getParameters() {
        return parameters;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public String toString() {
        return "RunOptions{" +
                "report uri=" + reportUri +
                ", parameters=" + parameters +
                '}';
    }

    public static class Builder {
        private String reportUri;
        private List<ReportParameter> parameters;

        public Builder() {}

        private Builder(RunOptions options) {
            this.reportUri = options.reportUri;
            this.parameters = options.parameters;
        }

        public Builder parameters(List<ReportParameter> parameters) {
            this.parameters = Collections.unmodifiableList(parameters);
            return this;
        }

        public Builder reportUri(String reportUri) {
            this.reportUri = reportUri;
            return this;
        }

        public RunOptions build() {
            if (parameters == null) {
                parameters = Collections.emptyList();
            }
            return new RunOptions(reportUri, parameters);
        }
    }
}
