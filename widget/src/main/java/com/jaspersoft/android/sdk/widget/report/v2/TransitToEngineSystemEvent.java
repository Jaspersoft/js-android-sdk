package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class TransitToEngineSystemEvent implements SystemEvent {
    private final double code;
    private final RunOptions options;

    TransitToEngineSystemEvent(double code, RunOptions options) {
        this.code = code;
        this.options = options;
    }

    public double getCode() {
        return code;
    }

    public RunOptions getOptions() {
        return options;
    }
}
