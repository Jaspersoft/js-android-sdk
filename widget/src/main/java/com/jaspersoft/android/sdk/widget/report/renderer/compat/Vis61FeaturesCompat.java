package com.jaspersoft.android.sdk.widget.report.renderer.compat;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class Vis61FeaturesCompat extends ReportFeaturesCompat {
    @Override
    public boolean isSupported(ReportFeature reportFeature) {
        switch (reportFeature) {
            case ANCHOR_NAVIGATION:
                return true;
            case DATA_REFRESHING:
                return true;
            case PARAMS_APPLYING:
                return true;
            default:
                return false;
        }
    }
}
