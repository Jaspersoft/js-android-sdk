package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("dashboardParameter")
    private DashboardParameters params;

    public DashboardExportExecutionOptions(String uri, Integer width, Integer height, String format, DashboardParameters params) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri can not be null!");
        }

        if (format == null) {
            throw new IllegalArgumentException("Format can not be null!");
        }

        this.uri = uri;
        this.width = width;
        this.height = height;
        this.format = format;
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DashboardExportExecutionOptions that = (DashboardExportExecutionOptions) o;

        if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;
        if (width != null ? !width.equals(that.width) : that.width != null) return false;
        if (height != null ? !height.equals(that.height) : that.height != null) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        return params != null ? params.equals(that.params) : that.params == null;

    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (format != null ? format.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }
}
