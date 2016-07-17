package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class LollipopWebResourceInterceptor implements WebResourceInterceptor.Rule {
    private static final String[] RESOURCES = new String[]{"bundles", "scripts", "settings"};

    @Override
    public boolean shouldIntercept(WebRequest request) {
        boolean defaultVale = false;
        String url = request.getUrl().toLowerCase();
        for (String resource : RESOURCES) {
            defaultVale |= url.contains(resource);
        }
        return defaultVale;
    }
}
