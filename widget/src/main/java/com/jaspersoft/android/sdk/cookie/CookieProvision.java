package com.jaspersoft.android.sdk.cookie;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.CookieSyncManager;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieStore;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class CookieProvision {
    private static CookieProvision _instance;

    private final java.net.CookieManager cookieManager;

    private CookieProvision(Context context) {
        cookieManager = newStore(context);
    }

    public static java.net.CookieManager provideHandler(Context context) {
        if (_instance == null) {
            synchronized (CookieProvision.class) {
                if (_instance == null) {
                    _instance = new CookieProvision(context.getApplicationContext());
                }
            }
        }
        return _instance.cookieManager;
    }

    public static void clearAll(Context context) {
        java.net.CookieManager cookieManager = provideHandler(context);
        CookieStore cookieStore = cookieManager.getCookieStore();
        cookieStore.removeAll();
    }

    @NonNull
    private java.net.CookieManager newStore(@NonNull Context context) {
        WebViewCookieStore webViewCookieStore = createWebViewCookieStore(context);
        CookieStore cookieStore = new PersistentCookieStore(context);

        AppCookieStore appCookieStore = new AppCookieStore(webViewCookieStore, cookieStore);

        java.net.CookieManager defaultCookieManager = new java.net.CookieManager(appCookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(defaultCookieManager);

        return defaultCookieManager;
    }

    private WebViewCookieStore createWebViewCookieStore(@NonNull Context context) {
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new WebViewCookieStoreLollipop(cookieManager);
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            return new WebViewCookieStorePreLollipop(cookieManager, cookieSyncManager);
        }
    }
}
