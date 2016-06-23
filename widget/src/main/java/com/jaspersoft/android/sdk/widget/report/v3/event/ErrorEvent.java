package com.jaspersoft.android.sdk.widget.report.v3.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ErrorEvent implements Event {
    private final JsException exception;

    ErrorEvent(JsException exception) {
        this.exception = exception;
    }

    public JsException getError() {
        return exception;
    }
}
