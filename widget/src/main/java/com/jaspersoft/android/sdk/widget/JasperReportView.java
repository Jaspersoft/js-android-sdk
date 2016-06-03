package com.jaspersoft.android.sdk.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class JasperReportView extends FrameLayout {

    private WebView webView;
    private InflateCallback inflateCallback;
    private AuthorizedClient client;
    private ReportFacade container;

    public JasperReportView(Context context) {
        super(context);
        setupWebView();
    }

    public JasperReportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupWebView();
    }

    public JasperReportView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupWebView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JasperReportView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupWebView();
    }

    public void init(AuthorizedClient client, InflateCallback callback) {
        container = new ReportFacade.Builder(client, webView).build();
        container.init(callback);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        webView = new WebView(getContext());
        webView.getSettings().setJavaScriptEnabled(true);
        addView(webView);
    }

    public interface InflateCallback {
        InflateCallback NULL = new InflateCallback() {
            @Override
            public void onStartInflate() {
            }

            @Override
            public void onFinishInflate() {
            }
        };

        void onStartInflate();
        void onFinishInflate();
    }
}
