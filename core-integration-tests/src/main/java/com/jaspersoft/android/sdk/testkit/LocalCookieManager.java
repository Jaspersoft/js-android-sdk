package com.jaspersoft.android.sdk.testkit;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public enum LocalCookieManager {
    INSTANCE;

    private final CookieManager mCookieManager;

    LocalCookieManager() {
        mCookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    public static CookieHandler get() {
        return INSTANCE.mCookieManager;
    }
}
