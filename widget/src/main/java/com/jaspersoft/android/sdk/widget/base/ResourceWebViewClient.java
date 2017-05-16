package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jaspersoft.android.sdk.widget.base.client.OkHttpWebResourceLoader;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ResourceWebViewClient extends WebViewClient {
    private final static WebClientEventCallback EMPTY = new WebClientEventCallback() {
        @Override
        public void onIntercept(String uri) {

        }

        @Override
        public void onWebError(int errorCode, String failingUrl) {

        }
    };

    private final WebClientEventCallback webClientEventCallback;
    private final WebResourceLoader requestLoader;
    private final NativeWebRequestMapper nativeWebRequestMapper;
    private final NativeWebResponseMapper nativeWebResponseMapper;
    private final LoadRule loadRule;

    private ResourceWebViewClient(WebClientEventCallback webClientEventCallback, WebResourceLoader requestLoader, NativeWebRequestMapper nativeWebRequestMapper, NativeWebResponseMapper nativeWebResponseMapper, LoadRule loadRule) {
        this.webClientEventCallback = webClientEventCallback;
        this.requestLoader = requestLoader;
        this.nativeWebRequestMapper = nativeWebRequestMapper;
        this.nativeWebResponseMapper = nativeWebResponseMapper;
        this.loadRule = loadRule;
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
        WebResponse webResponse = null;
        if (loadRule.shouldInterceptLoading(request)) {
            webResponse = requestLoader.load(view, request);
        }
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
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        webClientEventCallback.onWebError(errorCode, failingUrl);
    }

    public interface WebClientEventCallback {
        void onIntercept(String uri);

        void onWebError(int errorCode, String failingUrl);
    }

    public static class Builder {
        private boolean cacheEnabled;
        private WebClientEventCallback webClientEventCallback;

        public Builder() {
            cacheEnabled = true;
        }

        public Builder withEventListener(WebClientEventCallback webClientEventCallback) {
            this.webClientEventCallback = webClientEventCallback;
            return this;
        }

        public Builder withCacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
            return this;
        }

        public ResourceWebViewClient build(Context context) {
            WebResourceLoader webResourceLoader = OkHttpWebResourceLoader.create(context, cacheEnabled);
            NativeWebRequestMapper nativeWebRequestMapper = new NativeWebRequestMapper();
            NativeWebResponseMapper nativeWebResponseMapper = new NativeWebResponseMapper();
            LoadRule loadRule = new LoadRule();
            return new ResourceWebViewClient(webClientEventCallback != null ? webClientEventCallback : EMPTY, webResourceLoader,
                    nativeWebRequestMapper, nativeWebResponseMapper, loadRule);
        }
    }
}
