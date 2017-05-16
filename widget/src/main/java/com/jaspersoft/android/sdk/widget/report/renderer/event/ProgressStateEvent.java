package com.jaspersoft.android.sdk.widget.report.renderer.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ProgressStateEvent implements Event {
    private boolean inProgress;

    ProgressStateEvent(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isInProgress() {
        return inProgress;
    }
}
