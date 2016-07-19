package com.jaspersoft.android.sdk.widget.base;

import java.io.InputStream;
import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface WebResponse {
    String getMimeType();

    String getEncoding();

    InputStream getData();

    int getStatusCode();

    String getReasonPhrase();

    Map<String, String> getResponseHeaders();
}
