package com.jaspersoft.android.sdk.widget.base;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class CachedOkHttpClient extends OkHttpClient {
    private static CachedOkHttpClient ok;

    private final static String CACHE_FOLDER_NAME = "jsRequestCache";
    private final static int CACHE_SIZE = 52428800; // 50 MB

    private CachedOkHttpClient() {
    }

    public static CachedOkHttpClient getInstance(Context context) {
        if (ok == null) {
            ok = create(context);
        }
        return ok;
    }

    public static CachedOkHttpClient create(Context context) {
        CachedOkHttpClient client = new CachedOkHttpClient();
        File cacheDir = context.getApplicationContext().getExternalCacheDir();
        File requestCacheDir = new File(cacheDir, CACHE_FOLDER_NAME);
        if (requestCacheDir.exists() || requestCacheDir.mkdirs()) {
            Cache cache = new Cache(requestCacheDir, CACHE_SIZE);
            client.setCache(cache);
        }

        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String cacheHeaderValue = "no-transform,max-age=31536000";
                Response response = chain.proceed(chain.request());
                return response.newBuilder()
                        .header("Cache-Control", cacheHeaderValue)
                        .build();
            }
        });

        return client;
    }
}
