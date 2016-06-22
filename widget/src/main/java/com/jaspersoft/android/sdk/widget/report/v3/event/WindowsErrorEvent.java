package com.jaspersoft.android.sdk.widget.report.v3.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class WindowsErrorEvent implements Event {
    private final String errorLog;

    public WindowsErrorEvent(String errorLog) {
        this.errorLog = errorLog;
    }
}
