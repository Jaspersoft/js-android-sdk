package com.jaspersoft.android.sdk.widget.base;

import android.os.Build;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class LegacyWebResourceInterceptor implements WebResourceInterceptor.Rule {
    @Override
    public boolean shouldIntercept(WebRequest request) {
        return false;
    }
}
