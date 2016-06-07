package com.jaspersoft.android.sdk.sample;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ResourceFactory {

    private final Profile profile;

    public ResourceFactory(Profile profile) {
        this.profile = profile;
    }

    public Resource newReport(String label, String uri) {
        return Resource.newReport(uri, label, profile);
    }

    public Resource newDashboard(String label, String uri) {
        return Resource.newDashboard(uri, label, profile);
    }
}
