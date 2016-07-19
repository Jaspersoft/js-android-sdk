package com.jaspersoft.android.sdk.widget.base.client;

import com.jaspersoft.android.sdk.widget.base.WebRequest;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class LoadRule {
    private static final String[] RESOURCES = new String[]{"bundles", "scripts", "/client/visualize.js?_opt=true"};

    public boolean shouldInterceptLoading(WebRequest webRequest) {
        boolean defaultVale = false;
        String url = webRequest.getUrl().toLowerCase();
        for (String resource : RESOURCES) {
            defaultVale |= url.contains(resource);
        }
        return defaultVale;
    }
}
