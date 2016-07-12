package com.jaspersoft.android.sdk.widget.report.renderer.event;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class MultiPageStateChangedEvent implements Event {
    private final boolean isMultiPage;

    public MultiPageStateChangedEvent(boolean isMultiPage) {
        this.isMultiPage = isMultiPage;
    }

    public boolean isMultiPage() {
        return isMultiPage;
    }
}
