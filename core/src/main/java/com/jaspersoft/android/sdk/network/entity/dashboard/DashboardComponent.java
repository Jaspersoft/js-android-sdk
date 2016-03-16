package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class DashboardComponent {
    @Expose
    private String id;
    @Expose
    private String type;
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
