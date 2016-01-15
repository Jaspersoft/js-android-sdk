package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class JobDescriptor {
    @Expose
    private String id;
    @Expose
    private int version;
    @Expose
    private String username;
    @Expose
    private String label;
    @Expose
    private String description;
    @Expose
    private String creationDate;
    @Expose
    private JobOutputFormats outputFormats;

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getUsername() {
        return username;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public JobOutputFormats getOutputFormats() {
        return outputFormats;
    }
}
