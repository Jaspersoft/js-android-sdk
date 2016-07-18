package com.jaspersoft.android.sdk.widget.base;

import android.webkit.WebResourceResponse;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
abstract class NativeWebResponseMapper {
    public final WebResourceResponse toNativeResponse(WebResponse webResponse) {
        if (webResponse == null) return null;
        return internalToNativeResponse(webResponse);
    }

    protected abstract WebResourceResponse internalToNativeResponse(WebResponse webResponse);
}
