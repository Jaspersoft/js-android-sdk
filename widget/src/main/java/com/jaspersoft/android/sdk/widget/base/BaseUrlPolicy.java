package com.jaspersoft.android.sdk.widget.base;

import android.net.Uri;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class BaseUrlPolicy implements UrlPolicy {
    private final String serverUrl;

    public BaseUrlPolicy(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public boolean isLoginPageLink(String url) {
        String jasperHost = Uri.parse(serverUrl).getHost();
        String linkHost = Uri.parse(url).getHost();

        // This is JasperSoft site, let WebView check page for 401 page
        if (linkHost != null && linkHost.equals(jasperHost)) {
            if (url.contains("login.html")) {
                return true;

            }
        }
        return false;
    }
}
