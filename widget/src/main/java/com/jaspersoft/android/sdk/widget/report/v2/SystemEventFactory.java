package com.jaspersoft.android.sdk.widget.report.v2;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class SystemEventFactory {
    public SystemEvent createTransitToEngineEvent(double code, RunOptions options) {
        return new TransitToEngineSystemEvent(code, options);
    }

    public SystemEvent createInflateCompleteEvent(RunOptions options) {
        return new InflateCompleteSystemEvent(options);
    }
}
