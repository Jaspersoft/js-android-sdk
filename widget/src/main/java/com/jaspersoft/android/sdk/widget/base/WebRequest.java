package com.jaspersoft.android.sdk.widget.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public interface WebRequest {
    @NonNull
    String getUrl();

    @Nullable
    String getMethod();

    @NonNull
    Map<String, String> getRequestHeaders();
}
