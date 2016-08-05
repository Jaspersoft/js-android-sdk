package com.jaspersoft.android.sdk.widget.report.renderer;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;

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
    private final static String RESOURCE_NOT_FOUND_CODE = "resource.not.found";

    public ServiceException map(JsException exception) {
        if (AUTH_ERROR_CODE.equals(exception.errorCode)) {
            return new ServiceException("User is not authorized", new Throwable(exception.errorMessage), StatusCodes.AUTHORIZATION_ERROR);
        } else if (PAGE_OUT_OF_RANGE_CODE.equals(exception.errorCode) || EXPORT_PAGE_OUT_OF_RANGE_CODE.equals(exception.errorCode)) {
            return new ServiceException(exception.errorMessage, new Throwable(exception.errorMessage), StatusCodes.EXPORT_PAGE_OUT_OF_RANGE);
        } else if (ILLEGAL_PARAMETER_VALUE_CODE.equals(exception.errorCode) && "Value of parameter 'anchor' invalid".equals(exception.errorMessage)) {
            return new ServiceException("Page with requested anchor doest not exist", new Throwable(exception.errorMessage), StatusCodes.EXPORT_ANCHOR_ABSENT);
        } else if (RESOURCE_NOT_FOUND_CODE.equals(exception.errorCode)) {
            return new ServiceException(exception.errorMessage, new Throwable(exception.errorMessage), StatusCodes.RESOURCE_NOT_FOUND);
        }
        return new ServiceException("The operation failed with no more detailed information", new Throwable(exception.errorMessage), StatusCodes.UNDEFINED_ERROR);
    }
}
