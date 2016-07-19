package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceResponse;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class NativeWebResponseMapper{
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WebResourceResponse toNativeResponse(WebResponse webResponse) {
        if (webResponse == null) return null;

        return new WebResourceResponse(
                webResponse.getMimeType(),
                webResponse.getEncoding(),
                webResponse.getStatusCode(),
                webResponse.getReasonPhrase(),
                webResponse.getResponseHeaders(),
                webResponse.getData()
        );
    }
}
