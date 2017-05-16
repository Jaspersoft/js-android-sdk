package com.jaspersoft.android.sdk.widget.base.client;

import android.content.Context;

import com.jaspersoft.android.sdk.widget.base.RequestCacheStore;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class CachedOkHttpClient extends OkHttpClient {
    private static CachedOkHttpClient instance;

    private CachedOkHttpClient() {
    }

    public static CachedOkHttpClient getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static CachedOkHttpClient create(Context context) {
        CachedOkHttpClient client = new CachedOkHttpClient();
        File requestCacheDir = RequestCacheStore.getRequestCacheDir(context);
        if (requestCacheDir.exists() || requestCacheDir.mkdirs()) {
            Cache cache = new Cache(requestCacheDir, RequestCacheStore.getCacheSize());
            client.setCache(cache);
        }

        return client;
    }
}