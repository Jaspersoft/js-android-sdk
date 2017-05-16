package com.jaspersoft.android.sdk.service.data.dashboard;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardExportOptions {
    private final String uri;
    private final Integer width;
    private final Integer height;
    private final DashboardExportFormat format;
    private final List<ReportParameter> parameters;

    DashboardExportOptions(String uri, Integer width, Integer height, DashboardExportFormat format, List<ReportParameter> parameters) {
        this.uri = uri;
        this.width = width;
        this.height = height;
        this.format = format;
        this.parameters = parameters;
    }

    public String getUri() {
        return uri;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public DashboardExportFormat getFormat() {
        return format;
    }

    public List<ReportParameter> getParameters() {
        return parameters;
    }

    public static class Builder {
        private String uri;
        private Integer width;
        private Integer height;
        private DashboardExportFormat format;
        private List<ReportParameter> parameters;

        public Builder(String uri, DashboardExportFormat format) {
            this.uri = uri;
            this.format = format;
        }

        public Builder setWidth(Integer width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(Integer height) {
            this.height = height;
            return this;
        }

        public Builder setParameters(List<ReportParameter> parameters) {
            this.parameters = parameters;
            return this;
        }

        public DashboardExportOptions build () {
            return new DashboardExportOptions(uri, width, height, format, parameters);
        }
    }
}
