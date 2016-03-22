package com.jaspersoft.android.sdk.testkit;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ListResourcesUrisCommand {
    private final int count;
    private final String type;

    public ListResourcesUrisCommand(int count, String type) {
        this.count = count;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public String getType() {
        return type;
    }
}
