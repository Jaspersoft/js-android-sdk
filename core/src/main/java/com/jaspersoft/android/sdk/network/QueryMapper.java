package com.jaspersoft.android.sdk.network;

import com.squareup.okhttp.HttpUrl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.3
 */
enum QueryMapper {
    INSTANCE;

    public HttpUrl mapParams(Map<String, Object> searchParams, HttpUrl url) throws IOException {
        if (searchParams == null) {
            searchParams = Collections.emptyMap();
        }

        HttpUrl.Builder urlBuilder = url.newBuilder();

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();

            if (value instanceof Iterable<?>) {
                Iterable<?> iterable = (Iterable<?>) value;
                for (Object v : iterable) {
                    String encodedValue = encodeValue(v);
                    urlBuilder.addEncodedQueryParameter(key, encodedValue);
                }
            } else {
                String encodedValue = encodeValue(value);
                urlBuilder.addEncodedQueryParameter(key, encodedValue);
            }
        }

        return urlBuilder.build();
    }

    private String encodeValue(Object value) throws UnsupportedEncodingException {
        String rawValue = String.valueOf(value);
        return URLEncoder.encode(rawValue, "UTF-8");
    }
}
