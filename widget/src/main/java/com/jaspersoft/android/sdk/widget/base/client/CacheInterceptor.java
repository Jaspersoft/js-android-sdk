package com.jaspersoft.android.sdk.widget.base.client;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class CacheInterceptor implements Interceptor{
    private static final int CACHE_LIFE_TIME = 604800; // 1 week

    @Override
    public Response intercept(Chain chain) throws IOException {
        String cacheHeaderValue = "max-age=" + CACHE_LIFE_TIME;
        Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .header("Cache-Control", cacheHeaderValue)
                .build();
    }
}
