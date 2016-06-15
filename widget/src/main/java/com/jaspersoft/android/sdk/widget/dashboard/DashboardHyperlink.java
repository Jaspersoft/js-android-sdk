package com.jaspersoft.android.sdk.widget.dashboard;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public abstract class DashboardHyperlink {
    public enum Type {
        REPORT_EXECUTION, REFERENCE, ADHOC_EXECUTION;
    }

    private final Type type;

    protected DashboardHyperlink(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    static abstract class Factory<H extends DashboardHyperlink> {
        abstract H createLink(String data);
    }
}
