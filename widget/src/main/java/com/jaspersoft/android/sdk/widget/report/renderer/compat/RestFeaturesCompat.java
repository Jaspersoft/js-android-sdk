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
                return true;
            case PARAMS_APPLYING:
                return true;
            default:
                return false;
        }
    }
}
