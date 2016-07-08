package com.jaspersoft.android.sdk.widget;

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
public class ResourceWebView extends WebView {
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
    }
}
