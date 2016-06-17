package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class RunOptions {

    private final int page;
    private final String anchor;
    private final List<ReportParameter> parameters;

    private RunOptions(Builder builder) {
        this.page = builder.page;
        this.anchor = builder.anchor;
        this.parameters = builder.parameters;
    }

    public int getPage() {
        return page;
    }

    public String getAnchor() {
        return anchor;
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
                "page=" + page +
                ", anchor='" + anchor + '\'' +
                ", parameters=" + parameters +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RunOptions that = (RunOptions) o;

        if (page != that.page) return false;
        if (anchor != null ? !anchor.equals(that.anchor) : that.anchor != null) return false;
        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;

    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + (anchor != null ? anchor.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }

    public static class Builder {
        private int page = Integer.MIN_VALUE;
        private String anchor;
        private List<ReportParameter> parameters;

        public Builder() {}

        private Builder(RunOptions options) {
            this.page = options.page;
            this.anchor = options.anchor;
        }

        public Builder parameters(List<ReportParameter> parameters) {
            this.parameters = Collections.unmodifiableList(parameters);
            return this;
        }

        public Builder page(int page) {
            this.page = page;
            return this;
        }

        public Builder anchor(String anchor) {
            this.anchor = anchor;
            return this;
        }

        public RunOptions build() {
            if (page == Integer.MIN_VALUE && anchor == null) {
                throw new IllegalArgumentException("There should be either anchor, page or both.");
            }
            if (parameters == null) {
                parameters = Collections.emptyList();
            }
            return new RunOptions(this);
        }
    }
}
