package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class TransitToEngineEvent {
    private final double code;
    private final RunOptions options;

    public TransitToEngineEvent(double code, RunOptions options) {
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
