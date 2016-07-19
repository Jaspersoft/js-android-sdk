package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebResourceRequest;

import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class NativeWebRequestMapper {
    @NonNull
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WebRequest toGenericRequest(final WebResourceRequest request) {
        return new WebRequest() {
            @NonNull
            @Override
            public String getUrl() {
                return request.getUrl().toString();
            }

            @Nullable
            @Override
            public String getMethod() {
                return request.getMethod();
            }

            @NonNull
            @Override
            public Map<String, String> getRequestHeaders() {
                return request.getRequestHeaders();
            }
        };
    }
}
