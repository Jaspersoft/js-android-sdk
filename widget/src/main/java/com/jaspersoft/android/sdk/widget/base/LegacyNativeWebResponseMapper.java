package com.jaspersoft.android.sdk.widget.base;

import android.webkit.WebResourceResponse;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class LegacyNativeWebResponseMapper extends NativeWebResponseMapper {
    @Override
    protected WebResourceResponse internalToNativeResponse(WebResponse webResponse) {
        return new WebResourceResponse(
                webResponse.getMimeType(),
                webResponse.getEncoding(),
                webResponse.getData()
        );
    }
}
