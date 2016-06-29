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
    private final Destination destination;

    RunOptions(String reportUri, List<ReportParameter> parameters, Destination destination) {
        this.reportUri = reportUri;
        this.parameters = parameters;
        this.destination = destination;
    }

    public String getReportUri() {
        return reportUri;
    }

    public List<ReportParameter> getParameters() {
        return parameters;
    }

    public Destination getDestination() {
        return destination;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public String toString() {
        return "RunOptions{" +
                "report uri=" + reportUri +
                ", parameters=" + parameters +
                ", destination=" + destination +
                '}';
    }

    public static class Builder {
        private String reportUri;
        private List<ReportParameter> parameters;
        private Destination destination;

        public Builder() {}

        private Builder(RunOptions options) {
            this.reportUri = options.reportUri;
            this.parameters = options.parameters;
            this.destination = options.destination;
        }

        public Builder parameters(List<ReportParameter> parameters) {
            this.parameters = Collections.unmodifiableList(parameters);
            return this;
        }

        public Builder reportUri(String reportUri) {
            this.reportUri = reportUri;
            return this;
        }

        public Builder destination(Destination destination) {
            this.destination = destination;
            return this;
        }

        public RunOptions build() {
            if (reportUri == null || reportUri.isEmpty()) {
                throw new IllegalArgumentException("Report uri should be provided");
            }
            if (parameters == null) {
                parameters = Collections.emptyList();
            }
            if (destination == null) {
                destination = new Destination(1);
            }
            return new RunOptions(reportUri, parameters, destination);
        }
    }
}
