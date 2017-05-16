package com.jaspersoft.android.sdk.widget.base.client;

import android.support.annotation.Nullable;

import com.jaspersoft.android.sdk.widget.base.WebRequest;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;

import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class OkHttpClientRequestMapper {
    @Nullable
    public Request toOkHttpRequest(WebRequest request) {
        String url = request.getUrl();
        HttpUrl proxyUrl = HttpUrl.parse(url);
        if (proxyUrl == null) {
            return null;
        }

        Request.Builder requestBuilder = new Request.Builder()
                .get()
                .url(proxyUrl);

        Map<String, String> requestHeaders = request.getRequestHeaders();
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        return requestBuilder.build();
    }
}
