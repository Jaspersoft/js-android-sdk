package com.jaspersoft.android.sdk.widget.report.renderer.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class CurrentPageChangedEvent implements Event {
    private int currentPage;

    CurrentPageChangedEvent(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
