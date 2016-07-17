package com.jaspersoft.android.sdk.widget.base;

import android.webkit.WebView;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
abstract class WebResourceInterceptor {
    private final Rule interceptRule;

    public WebResourceInterceptor(Rule interceptRule) {
        this.interceptRule = interceptRule;
    }

    protected final boolean shouldIntercept(WebRequest webRequest) {
        return interceptRule.shouldIntercept(webRequest);
    }

    abstract WebResponse interceptRequest(WebView view, WebRequest request);

    interface Rule {
        boolean shouldIntercept(WebRequest request);
    }
}
