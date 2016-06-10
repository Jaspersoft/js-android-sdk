package com.jaspersoft.android.sdk.cookie;

import android.content.Context;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class RestCookieManager extends CookieManager {
    private RestCookieManager(CookieStore store, CookiePolicy cookiePolicy) {
        super(store, cookiePolicy);
    }

    public static class Builder {
        private final Context context;
        private boolean handleWebViewCookies;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder handleWebViewCookies(boolean handle) {
            handleWebViewCookies = handle;
            return this;
        }

        public java.net.CookieManager build() {
            WebViewCookieStore webViewCookieStore = handleWebViewCookies ? WebViewCookieStore.newInstance(context) : NoWebViewCookieStore.INSTANCE;
            CookieStore cookieStore = new PersistentCookieStore(context);

            AppCookieStore appCookieStore = new AppCookieStore(webViewCookieStore, cookieStore);

            java.net.CookieManager defaultCookieManager = new RestCookieManager(appCookieStore, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(defaultCookieManager);

            return defaultCookieManager;
        }
    }

    private static class NoWebViewCookieStore extends WebViewCookieStore {
        private final static WebViewCookieStore INSTANCE = new NoWebViewCookieStore();
    }
}
