package com.jaspersoft.android.sdk.sample.cookie;


import com.jaspersoft.android.sdk.network.AuthenticationLifecycle;

import java.net.CookieManager;
import java.net.CookieStore;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class CookieAuthenticationHandler implements AuthenticationLifecycle {
    private CookieManager cookieManager;

    public CookieAuthenticationHandler(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    @Override
    public void beforeSessionReload() {
        CookieStore cookieStore = cookieManager.getCookieStore();
        if (cookieStore != null) {
            cookieStore.removeAll();
        }
    }

    @Override
    public void afterSessionReload() {
    }
}
