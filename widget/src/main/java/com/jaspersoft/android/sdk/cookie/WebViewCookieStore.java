package com.jaspersoft.android.sdk.cookie;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.CookieSyncManager;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class WebViewCookieStore {

    public void add(@NonNull String domain, @NonNull String cookie) {
        Log.d("WebViewCookieStore", String.format("#add domain %s cookie %s", domain, cookie));
    }

    public void removeCookie(@NonNull String domain) {
        Log.d("WebViewCookieStore", String.format("#removeCookie domain %s", domain));
    }

    public void removeAllCookies() {
        Log.d("WebViewCookieStore", "#removeAllCookies");
    }

    public static WebViewCookieStore newInstance(Context context) {
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new WebViewCookieStoreLollipop(cookieManager);
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            return new WebViewCookieStorePreLollipop(cookieManager, cookieSyncManager);
        }
    }
}
