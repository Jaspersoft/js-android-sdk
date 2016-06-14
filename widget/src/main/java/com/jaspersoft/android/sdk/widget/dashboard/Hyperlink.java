package com.jaspersoft.android.sdk.widget.dashboard;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public abstract class Hyperlink {
    public enum Type {
        REPORT_EXECUTION, REFERENCE, ADHOC_EXECUTION;
    }

    private final Type type;

    protected Hyperlink(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    static abstract class Factory<H extends Hyperlink> {
        abstract H createLink(String data);
    }
}
