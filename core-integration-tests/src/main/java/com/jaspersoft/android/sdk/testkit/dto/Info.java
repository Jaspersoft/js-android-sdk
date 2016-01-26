package com.jaspersoft.android.sdk.testkit.dto;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class Info {
    @Expose
    private final String edition;
    @Expose
    private final String version;

    public Info(String edition, String version) {
        this.edition = edition;
        this.version = version;
    }

    public String getEdition() {
        return edition;
    }

    public String getVersion() {
        return version;
    }
}
