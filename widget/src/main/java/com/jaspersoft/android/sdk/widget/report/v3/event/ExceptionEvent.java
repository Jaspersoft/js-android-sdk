package com.jaspersoft.android.sdk.widget.report.v3.event;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ExceptionEvent implements Event{
    private final Exception exception;

    public ExceptionEvent(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
