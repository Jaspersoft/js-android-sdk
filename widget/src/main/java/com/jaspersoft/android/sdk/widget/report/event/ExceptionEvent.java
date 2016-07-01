package com.jaspersoft.android.sdk.widget.report.event;

import com.jaspersoft.android.sdk.service.exception.ServiceException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ExceptionEvent implements Event{
    private final ServiceException exception;

    ExceptionEvent(ServiceException exception) {
        this.exception = exception;
    }

    public ServiceException getException() {
        return exception;
    }
}
