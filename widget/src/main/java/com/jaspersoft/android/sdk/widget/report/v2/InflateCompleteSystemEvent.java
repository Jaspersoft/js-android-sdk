package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class InflateCompleteSystemEvent implements SystemEvent {
    private final RunOptions options;

    InflateCompleteSystemEvent(RunOptions options) {
        this.options = options;
    }

    public RunOptions getOptions() {
        return options;
    }
}
