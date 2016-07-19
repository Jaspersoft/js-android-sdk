package com.jaspersoft.android.sdk.widget.base.client;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class CachedOkHttpClient extends OkHttpClient {
    private static CachedOkHttpClient instance;

    private final static String CACHE_FOLDER_NAME = "jsRequestCache";
    private final static int CACHE_SIZE = 52428800; // 50 MB

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
        File cacheDir = context.getApplicationContext().getExternalCacheDir();
        File requestCacheDir = new File(cacheDir, CACHE_FOLDER_NAME);
        if (requestCacheDir.exists() || requestCacheDir.mkdirs()) {
            Cache cache = new Cache(requestCacheDir, CACHE_SIZE);
            client.setCache(cache);
        }

        return client;
    }
}