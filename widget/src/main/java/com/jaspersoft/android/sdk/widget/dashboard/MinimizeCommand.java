package com.jaspersoft.android.sdk.widget.dashboard;

import android.webkit.WebView;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class MinimizeCommand implements Command {
    private final WebView webView;

    public MinimizeCommand(WebView webView) {
        this.webView = webView;
    }

    public WebView getWebView() {
        return webView;
    }
}
