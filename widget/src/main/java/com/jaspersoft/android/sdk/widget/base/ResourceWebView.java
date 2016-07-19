package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ResourceWebView extends WebView implements ResourceWebViewClient.WebClientEventCallback {
    private final static ResourceWebViewEventListener EMPTY = new ResourceWebViewEventListener() {
        @Override
        public void onExternalLinkIntercept(String url) {

        }

        @Override
        public void onWebErrorObtain() {

        }
    };

    private ResourceWebViewEventListener resourceWebViewEventListener;

    public ResourceWebView(Context context) {
        super(context);
        init();
    }

    public ResourceWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ResourceWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ResourceWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setResourceWebViewEventListener(ResourceWebViewEventListener resourceWebViewEventListener) {
        this.resourceWebViewEventListener = resourceWebViewEventListener;
        if (this.resourceWebViewEventListener == null) {
            this.resourceWebViewEventListener = EMPTY;
        }
    }

    @Override
    public final WebSettings getSettings() {
        throw new UnsupportedOperationException("Configuring options is not supported");
    }

    private void init() {
        final WebSettings settings = super.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        setWebViewClient(new ResourceWebViewClient.Builder()
                .withEventListener(this)
                .withCacheEnabled(true)
                .build(getContext()));

        setResourceWebViewEventListener(null);
    }

    @Override
    public void onIntercept(String uri) {
        resourceWebViewEventListener.onExternalLinkIntercept(uri);
    }

    @Override
    public void onWebError(int errorCode, String failingUrl) {
        resourceWebViewEventListener.onWebErrorObtain();
    }

    public interface ResourceWebViewEventListener {
        void onExternalLinkIntercept(String url);

        void onWebErrorObtain();
    }
}
