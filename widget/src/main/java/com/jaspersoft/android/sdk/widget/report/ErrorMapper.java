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
    private final static String EXPORT_PAGE_OUT_OF_RANGE_CODE = "export.pages.out.of.range";
    private final static String PAGE_OUT_OF_RANGE_CODE = "page.number.out.of.range";
    private final static String ILLEGAL_PARAMETER_VALUE_CODE = "illegal.parameter.value.error";
    private final static String ANCHOR_NAVIGATION_NOT_SUPPORTED_CODE = "anchor.navigation.is.not.supported";
    private final static String RESOURCE_BOT_FOUND_CODE = "resource.not.found";

    public ServiceException map(JsException exception) {
        if (exception.errorCode.equals(AUTH_ERROR_CODE)) {
            return new ServiceException("User is not authorized", new Throwable(exception.errorMessage), StatusCodes.AUTHORIZATION_ERROR);
        } else if (exception.errorCode.equals(PAGE_OUT_OF_RANGE_CODE) || exception.errorCode.equals(EXPORT_PAGE_OUT_OF_RANGE_CODE)) {
            return new ServiceException(exception.errorMessage, new Throwable(exception.errorMessage), StatusCodes.EXPORT_PAGE_OUT_OF_RANGE);
        } else if (exception.errorCode.equals(ILLEGAL_PARAMETER_VALUE_CODE) && exception.errorMessage.equals("Value of parameter 'anchor' invalid")) {
            return new ServiceException("Page with requested anchor doest not exist", new Throwable(exception.errorMessage), StatusCodes.EXPORT_ANCHOR_ABSENT);
        } else if (exception.errorCode.equals(ANCHOR_NAVIGATION_NOT_SUPPORTED_CODE)) {
            return new ServiceException(exception.errorMessage, new Throwable(exception.errorMessage), StatusCodes.EXPORT_ANCHOR_UNSUPPORTED);
        } else if (exception.errorCode.equals(RESOURCE_BOT_FOUND_CODE)) {
            return new ServiceException(exception.errorMessage, new Throwable(exception.errorMessage), StatusCodes.RESOURCE_NOT_FOUND);
        }
        return new ServiceException("The operation failed with no more detailed information", new Throwable(exception.errorMessage), StatusCodes.UNDEFINED_ERROR);
    }
}
