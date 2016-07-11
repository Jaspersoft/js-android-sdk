package com.jaspersoft.android.sdk.widget.report.renderer.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class PagesCountChangedEvent implements Event {
    private Integer totalCount;

    PagesCountChangedEvent(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }
}
