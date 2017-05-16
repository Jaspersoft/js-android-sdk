package com.jaspersoft.android.sdk.widget.base.client;

import com.jaspersoft.android.sdk.widget.base.WebResponse;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class OkHttpClientResponseMapper {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_ENCODING = "Content-Encoding";

    public WebResponse toWebViewResponse(Response response) {
        final String mimeType = response.header(CONTENT_TYPE);
        final String encoding = response.header(CONTENT_ENCODING);
        final InputStream data = extractData(response);
        final int statusCode = response.code();
        final String reasonPhrase = response.message();
        final Map<String, String> responseHeaders = extractHeaders(response);

        return new WebResponse() {
            @Override
            public String getMimeType() {
                return mimeType;
            }

            @Override
            public String getEncoding() {
                return encoding;
            }

            @Override
            public InputStream getData() {
                return data;
            }

            @Override
            public int getStatusCode() {
                return statusCode;
            }

            @Override
            public String getReasonPhrase() {
                if (reasonPhrase == null || reasonPhrase.trim().isEmpty()) return "OK";
                return reasonPhrase;
            }

            @Override
            public Map<String, String> getResponseHeaders() {
                return responseHeaders;
            }
        };
    }

    private InputStream extractData(Response response) {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        try {
            return body.byteStream();
        } catch (IOException e) {
            return null;
        }
    }

    private Map<String, String> extractHeaders(Response response) {
        Map<String, List<String>> headers = response.headers().toMultimap();
        Map<String, String> extractedHeaders = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            List<String> value = entry.getValue();
            int size = value.size();
            extractedHeaders.put(entry.getKey(), value.get(size - 1));
        }
        return extractedHeaders;
    }
}
