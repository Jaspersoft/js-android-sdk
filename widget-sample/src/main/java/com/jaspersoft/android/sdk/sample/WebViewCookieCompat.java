package com.jaspersoft.android.sdk.sample;

import android.content.Context;

import com.jaspersoft.android.sdk.cookie.WebViewCookieStore;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class WebViewCookieCompat {
    private WebViewCookieCompat() {
        // no instance
    }

    public static void removeAllCookies(Context context) {
        WebViewCookieStore webViewCookieStore = WebViewCookieStore.newInstance(context);
        webViewCookieStore.removeAllCookies();
    }
}
