package com.jaspersoft.android.sdk.widget.report;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.report.event.JsException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ErrorMapper {

    ErrorMapper() {
    }

    private final static String AUTH_ERROR_CODE = "authentication.error";

    public ServiceException map(JsException exception) {
        if (exception.errorCode.equals(AUTH_ERROR_CODE)) {
            return new ServiceException("User is not authorized", new Throwable(exception.errorMessage), StatusCodes.AUTHORIZATION_ERROR);
        }
        return new ServiceException("The operation failed with no more detailed information", new Throwable(exception.errorMessage), StatusCodes.UNDEFINED_ERROR);
    }
}
