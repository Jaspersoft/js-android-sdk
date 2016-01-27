package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlDashboardComponent extends DashboardComponent {
    @Expose
    private String label;
    @Expose
    private String ownerResourceId;
    @Expose
    private String ownerResourceParameterName;
    @Expose
    private String parentId;
    @Expose
    private String resource;
    @Expose
    private int position;

    public String getLabel() {
        return label;
    }

    public String getOwnerResourceId() {
        return ownerResourceId;
    }

    public String getOwnerResourceParameterName() {
        return ownerResourceParameterName;
    }

    public String getParentId() {
        return parentId;
    }

    public String getResource() {
        return resource;
    }

    public int getPosition() {
        return position;
    }
}
