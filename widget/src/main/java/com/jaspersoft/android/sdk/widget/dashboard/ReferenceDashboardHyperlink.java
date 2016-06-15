package com.jaspersoft.android.sdk.widget.dashboard;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReferenceDashboardHyperlink extends DashboardHyperlink {
    private final String source;

    ReferenceDashboardHyperlink(String source) {
        super(Type.REFERENCE);
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
