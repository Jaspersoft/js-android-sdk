package com.jaspersoft.android.sdk.network.exception;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ErrorHandler<ERROR> {
    ErrorHandler DEFAULT = new RetrofitErrorHandler();

    Throwable handleError(ERROR cause);
}
