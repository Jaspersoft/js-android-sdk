package com.jaspersoft.android.sdk.cookie;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Tom Koptel
 * @since 2.3
 */
abstract class WebViewCookieStore {

    public void add(@NonNull String domain, @NonNull String cookie) {
        Log.d("WebViewCookieStore", String.format("#add domain %s cookie %s", domain, cookie));
    }

    public void removeCookie(@NonNull String domain) {
        Log.d("WebViewCookieStore", String.format("#removeCookie domain %s", domain));
    }

    public void removeAllCookies() {
        Log.d("WebViewCookieStore", "#removeAllCookies");
    }
}
