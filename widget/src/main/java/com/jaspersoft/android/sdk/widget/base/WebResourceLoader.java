package com.jaspersoft.android.sdk.widget.base;

import android.webkit.WebView;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface WebResourceLoader {
    WebResponse load(WebView view, WebRequest webRequest);
}
