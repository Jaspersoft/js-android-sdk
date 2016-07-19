package com.jaspersoft.android.sdk.widget.base.client;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.base.WebRequest;
import com.jaspersoft.android.sdk.widget.base.WebResourceLoader;
import com.jaspersoft.android.sdk.widget.base.WebResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class OkHttpWebResourceLoader implements WebResourceLoader {
    private final OkHttpClient client;
    private final OkHttpClientRequestMapper okHttpClientRequestMapper;
    private final OkHttpClientResponseMapper okHttpClientResponseMapper;

    private OkHttpWebResourceLoader(OkHttpClient client, OkHttpClientRequestMapper okHttpClientRequestMapper, OkHttpClientResponseMapper okHttpClientResponseMapper) {
        this.client = client;
        this.okHttpClientRequestMapper = okHttpClientRequestMapper;
        this.okHttpClientResponseMapper = okHttpClientResponseMapper;
    }

    public static OkHttpWebResourceLoader create(Context context, boolean cacheEnabled) {
        OkHttpClient okHttpClient = CachedOkHttpClient.getInstance(context);
        if (cacheEnabled) {
            okHttpClient.networkInterceptors().clear();
            okHttpClient.networkInterceptors().add(new CacheInterceptor());
        } else {
            okHttpClient.interceptors().clear();
            okHttpClient.interceptors().add(new NonCacheInterceptor());
        }

        OkHttpClientRequestMapper okHttpClientRequestMapper = new OkHttpClientRequestMapper();
        OkHttpClientResponseMapper okHttpClientResponseMapper = new OkHttpClientResponseMapper();
        return new OkHttpWebResourceLoader(okHttpClient, okHttpClientRequestMapper, okHttpClientResponseMapper);
    }

    @Override
    public WebResponse load(WebView view, WebRequest webRequest) {
        Request request = okHttpClientRequestMapper.toOkHttpRequest(webRequest);
        if (request == null) return null;

        try {
            Response response = client.newCall(request).execute();
            return okHttpClientResponseMapper.toWebViewResponse(response);
        } catch (IOException e) {
            return null;
        }
    }
}
