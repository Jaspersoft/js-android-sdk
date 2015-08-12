package com.jaspersoft.android.sdk.network.rest.v2.api;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface RestApiLog {
    void log(String message);

    RestApiLog NONE = new RestApiLog() {
        @Override public void log(String message) {
        }
    };
}
