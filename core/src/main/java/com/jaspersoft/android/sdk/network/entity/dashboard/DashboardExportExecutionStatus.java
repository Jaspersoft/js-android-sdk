package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class DashboardExportExecutionStatus {
    @Expose
    private String id;
    @Expose
    private int progress;
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public int getProgress() {
        return progress;
    }

    public String getId() {
        return id;
    }
}
