package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.service.exception.ServiceException;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class SdkErrorEvent {
    private final ServiceException exception;

    public SdkErrorEvent(ServiceException exception) {
        this.exception = exception;
    }
}
