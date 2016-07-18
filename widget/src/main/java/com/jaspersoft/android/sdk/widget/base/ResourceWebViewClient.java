package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ResourceWebViewClient extends WebViewClient {
    private final static WebClientEventCallback EMPTY = new WebClientEventCallback() {
        @Override
        public void onIntercept(String uri) {

        }

        @Override
        public void onWebError(int errorCode, String failingUrl) {

        }
    };

    private final WebClientEventCallback webClientEventCallback;
    private final WebResourceInterceptor requestInterceptor;
    private final NativeWebRequestMapper nativeWebRequestMapper;
    private final NativeWebResponseMapper nativeWebResponseMapper;

    ResourceWebViewClient(WebClientEventCallback webClientEventCallback, WebResourceInterceptor requestInterceptor, NativeWebRequestMapper nativeWebRequestMapper, NativeWebResponseMapper nativeWebResponseMapper) {
        this.webClientEventCallback = webClientEventCallback;
        this.requestInterceptor = requestInterceptor;
        this.nativeWebRequestMapper = nativeWebRequestMapper;
        this.nativeWebResponseMapper = nativeWebResponseMapper;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        webClientEventCallback.onIntercept(url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest webResourceRequest) {
        WebRequest request = nativeWebRequestMapper.toGenericRequest(webResourceRequest);
        WebResponse webResponse = intercept(view, request);
        WebResourceResponse webResourceResponse = nativeWebResponseMapper.toNativeResponse(webResponse);
        return webResourceResponse != null ? webResourceResponse : super.shouldInterceptRequest(view, webResourceRequest);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        webClientEventCallback.onWebError(error.getErrorCode(), request.getUrl().toString());
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        webClientEventCallback.onWebError(errorCode, failingUrl);
    }

    @Nullable
    private WebResponse intercept(WebView view, WebRequest request) {
        return requestInterceptor.interceptRequest(view, request);
    }

    public interface WebClientEventCallback {
        void onIntercept(String uri);
        void onWebError(int errorCode, String failingUrl);
    }

    public static class Builder {
        private WebClientEventCallback webClientEventCallback;

        public Builder withEventListener(WebClientEventCallback webClientEventCallback) {
            this.webClientEventCallback = webClientEventCallback;
            return this;
        }

        public ResourceWebViewClient build(Context context) {
            boolean preLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
            WebResourceInterceptor.Rule rule = preLollipop ? new LegacyWebResourceInterceptor() : new LollipopWebResourceInterceptor();
            CachedOkHttpClient cachedOkHttpClient = CachedOkHttpClient.getInstance(context);
            ClientRequestMapper clientRequestMapper = new ClientRequestMapper();
            ClientResponseMapper clientResponseMapper = new ClientResponseMapper();
            WebResourceInterceptor webResourceInterceptor = new ClientWebResourceInterceptor(rule, cachedOkHttpClient,
                    clientRequestMapper, clientResponseMapper);
            NativeWebRequestMapper nativeWebRequestMapper = new NativeWebRequestMapper();
            NativeWebResponseMapper nativeWebResponseMapper = preLollipop ? new LegacyNativeWebResponseMapper() : new LollipopNativeWebResponseMapper();
            return new ResourceWebViewClient(webClientEventCallback != null ? webClientEventCallback : EMPTY, webResourceInterceptor,
                    nativeWebRequestMapper, nativeWebResponseMapper);
        }
    }
}
