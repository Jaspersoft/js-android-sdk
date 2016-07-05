package com.jaspersoft.android.sdk.widget.report.renderer.compat;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class RestFeaturesCompat extends ReportFeaturesCompat {
    @Override
    public boolean isSupported(ReportFeature reportFeature) {
        switch (reportFeature) {
            case ANCHOR_NAVIGATION:
                return false;
            case DATA_REFRESHING:
                return false;
            case PARAMS_APPLYING:
                return false;
            default:
                return false;
        }
    }
}
