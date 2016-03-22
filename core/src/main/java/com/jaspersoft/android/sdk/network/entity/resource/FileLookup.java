package com.jaspersoft.android.sdk.network.entity.resource;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class FileLookup extends ResourceLookup {
    @Expose
    protected String type;

    public FileLookup() {
    }

    @Override
    public String getResourceType() {
        return "file";
    }

    public String getType() {
        return type;
    }
}
