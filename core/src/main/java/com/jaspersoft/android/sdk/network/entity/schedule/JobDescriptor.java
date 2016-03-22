package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobDescriptor {
    @Expose
    private int id;
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
    private JobOutputFormatsEntity outputFormats;

    public int getId() {
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

    public JobOutputFormatsEntity getOutputFormats() {
        return outputFormats;
    }
}
