package com.jaspersoft.android.sdk.sample.entity;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ResourceFactory {

    private final Profile profile;

    public ResourceFactory(Profile profile) {
        this.profile = profile;
    }

    public Resource newReport(String label, String uri, String params) {
        return Resource.newReport(uri, label, params, profile);
    }

    public Resource newReport(String label, String uri) {
        return Resource.newReport(uri, label, null, profile);
    }

    public Resource newDashboard(String label, String uri) {
        return Resource.newDashboard(uri, label, profile);
    }
}
