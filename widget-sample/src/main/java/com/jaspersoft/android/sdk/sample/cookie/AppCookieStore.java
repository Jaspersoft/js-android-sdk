package com.jaspersoft.android.sdk.sample.cookie;

import org.jetbrains.annotations.TestOnly;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class AppCookieStore implements CookieStore {
    private final WebViewCookieStore mWebViewCookieStore;
    private final CookieStore mStore;

    @TestOnly
    AppCookieStore(
            WebViewCookieStore webViewCookieStore,
            CookieStore persistentStore) {
        mWebViewCookieStore = webViewCookieStore;
        mStore = persistentStore;
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        mStore.add(uri, cookie);
        mWebViewCookieStore.add(uri.toString(), cookie.toString());
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return mStore.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return mStore.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return mStore.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        boolean result = mStore.remove(uri, cookie);
        mWebViewCookieStore.removeCookie(uri.toString());
        return result;
    }

    @Override
    public boolean removeAll() {
        boolean result = mStore.removeAll();
        mWebViewCookieStore.removeAllCookies();
        return result;
    }
}
