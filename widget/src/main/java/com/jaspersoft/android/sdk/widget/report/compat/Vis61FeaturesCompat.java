package com.jaspersoft.android.sdk.widget.report.compat;

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
            case FILTERS_APPLYING:
                return true;
            default:
                return false;
        }
    }
}
