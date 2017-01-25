

package com.jaspersoft.android.sdk.sample.cookie;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
final class WebViewCookieStorePreLollipop extends WebViewCookieStore {
    private final CookieManager mCookieManager;
    private final CookieSyncManager mCookieSyncManager;

    public WebViewCookieStorePreLollipop(CookieManager cookieManager,
                                         CookieSyncManager cookieSyncManager) {
        mCookieManager = cookieManager;
        mCookieSyncManager = cookieSyncManager;
    }

    @Override
    public void add(@NonNull String domain, @NonNull String cookie) {
        super.add(domain, cookie);
        mCookieManager.setCookie(domain, cookie);
        mCookieSyncManager.sync();
    }

    @Override
    public void removeCookie(@NonNull String domain) {
        super.removeCookie(domain);
        mCookieManager.setCookie(domain, null);
        mCookieSyncManager.sync();
    }

    @Override
    public void removeAllCookies() {
        super.removeAllCookies();
        mCookieManager.removeAllCookie();
        mCookieSyncManager.sync();
    }
}
