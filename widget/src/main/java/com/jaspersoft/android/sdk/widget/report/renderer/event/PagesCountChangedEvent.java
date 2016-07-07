package com.jaspersoft.android.sdk.widget.report.renderer.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class PagesCountChangedEvent implements Event {
    private int totalCount;

    PagesCountChangedEvent(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
