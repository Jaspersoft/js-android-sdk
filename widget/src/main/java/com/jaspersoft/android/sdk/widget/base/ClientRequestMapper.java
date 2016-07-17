package com.jaspersoft.android.sdk.widget.base;

import android.support.annotation.Nullable;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;

import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class ClientRequestMapper {
    private static final String PRAGMA = "Pragma";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String USER_AGENT = "User-Agent";
    private static final String[] HEADERS = new String[]{PRAGMA, CACHE_CONTROL, USER_AGENT};

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

        Map<String, String> requestHeaders = extractRequestHeaders(request);
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        return requestBuilder.build();
    }

    private Map<String, String> extractRequestHeaders(WebRequest request) {
        Map<String, String> requestHeaders = request.getRequestHeaders();
        filterHeaders(requestHeaders);
        return requestHeaders;
    }

    private void filterHeaders(Map<String, String> requestHeaders) {
        for (String header : HEADERS) {
            requestHeaders.remove(header);
        }
    }
}
