package com.jaspersoft.android.sdk.cookie;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.CookieManager;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
final class WebViewCookieStoreLollipop extends WebViewCookieStore {
    private final CookieManager mCookieManager;

    WebViewCookieStoreLollipop(CookieManager cookieManager) {
        mCookieManager = cookieManager;
    }

    @Override
    public void add(@NonNull String domain, @NonNull String cookie) {
        super.add(domain, cookie);
        mCookieManager.setCookie(domain, cookie);
        mCookieManager.flush();
    }

    @Override
    public void removeCookie(@NonNull String domain) {
        super.removeCookie(domain);
        mCookieManager.setCookie(domain, null);
        mCookieManager.flush();
    }

    @Override
    public void removeAllCookies() {
        super.removeAllCookies();
        mCookieManager.removeAllCookies(null);
        mCookieManager.flush();
    }
}
