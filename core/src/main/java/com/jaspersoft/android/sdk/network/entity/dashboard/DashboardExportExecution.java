package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardExportExecution {
    @Expose
    private String id;
    @Expose
    private String uri;
    @Expose
    private int width;
    @Expose
    private int height;
    @Expose
    private int referenceWidth;
    @Expose
    private int referenceHeight;
    @Expose
    private String format;
    @Expose
    @SerializedName("dashboardParameter")
    private DashboardParameters parameters;
    @Expose
    private List<String> jrStyle;

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getReferenceWidth() {
        return referenceWidth;
    }

    public int getReferenceHeight() {
        return referenceHeight;
    }

    public String getFormat() {
        return format;
    }

    public DashboardParameters getParameters() {
        return parameters;
    }

    public List<String> getJrStyle() {
        return jrStyle;
    }
}
