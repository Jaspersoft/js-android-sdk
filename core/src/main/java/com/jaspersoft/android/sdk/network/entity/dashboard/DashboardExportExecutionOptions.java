package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardExportExecutionOptions {
    @Expose
    private String uri;
    @Expose
    private Integer width;
    @Expose
    private Integer height;
    @Expose
    private String format;
    @Expose
    private String params;

    DashboardExportExecutionOptions(String uri, Integer width, Integer height, String format, String params) {
        this.uri = uri;
        this.width = width;
        this.height = height;
        this.format = format;
        this.params = params;
    }

    public static class Builder{
        private String uri;
        private Integer width;
        private Integer height;
        private String format;
        private String params;

        public Builder(String uri, String format) {
            if (uri == null) {
                throw new IllegalArgumentException("Uri can not be null!");
            }

            if (format == null) {
                throw new IllegalArgumentException("Format can not be null!");
            }

            this.uri = uri;
            this.format = format;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public DashboardExportExecutionOptions build() {
            return new DashboardExportExecutionOptions(uri, width, height, format, params);
        }
    }
}
