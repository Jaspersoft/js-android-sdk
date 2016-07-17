package com.jaspersoft.android.sdk.widget.base;

import android.webkit.WebView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class ClientWebResourceInterceptor extends WebResourceInterceptor {
    private final OkHttpClient client;
    private final ClientRequestMapper clientRequestMapper;
    private final ClientResponseMapper clientResponseMapper;

    public ClientWebResourceInterceptor(Rule interceptRule, OkHttpClient client, ClientRequestMapper clientRequestMapper, ClientResponseMapper clientResponseMapper) {
        super(interceptRule);
        this.client = client;
        this.clientRequestMapper = clientRequestMapper;
        this.clientResponseMapper = clientResponseMapper;
    }

    @Override
    WebResponse interceptRequest(WebView view, WebRequest request) {
        if (shouldIntercept(request)) {
            try {
                return intercept(request);
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private WebResponse intercept(WebRequest webRequest) throws IOException {
        Request request = clientRequestMapper.toOkHttpRequest(webRequest);
        if (request == null) {
            return null;
        } else {
            Response response = client.newCall(request).execute();
            return clientResponseMapper.toWebViewResponse(response);
        }
    }
}
