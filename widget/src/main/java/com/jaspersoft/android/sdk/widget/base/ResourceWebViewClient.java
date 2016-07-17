package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ResourceWebViewClient extends WebViewClient {
    private final WebResourceInterceptor requestInterceptor;
    private final NativeWebRequestMapper nativeWebRequestMapper;
    private final NativeWebResponseMapper nativeWebResponseMapper;
    private final UrlPolicy urlPolicy;

    ResourceWebViewClient(WebResourceInterceptor requestInterceptor, NativeWebRequestMapper nativeWebRequestMapper, NativeWebResponseMapper nativeWebResponseMapper, UrlPolicy urlPolicy) {
        this.requestInterceptor = requestInterceptor;
        this.nativeWebRequestMapper = nativeWebRequestMapper;
        this.nativeWebResponseMapper = nativeWebResponseMapper;
        this.urlPolicy = urlPolicy;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (urlPolicy.isLoginPageLink(url)) {

        } else {

        }
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

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebRequest request = nativeWebRequestMapper.toGenericRequest(url);
        WebResponse webResponse = intercept(view, request);
        WebResourceResponse webResourceResponse = nativeWebResponseMapper.toNativeResponse(webResponse);
        return webResourceResponse != null ? webResourceResponse : super.shouldInterceptRequest(view, url);
    }

    @Nullable
    private WebResponse intercept(WebView view, WebRequest request) {
        return requestInterceptor.interceptRequest(view, request);
    }
}
