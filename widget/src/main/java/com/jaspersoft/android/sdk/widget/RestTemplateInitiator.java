package com.jaspersoft.android.sdk.widget;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class RestTemplateInitiator extends TemplateInitiator{
    RestTemplateInitiator(AuthorizedClient client) {
        super(client, "report-rest-template.html");
    }

    @Override
    void beforeTemplateLoaded(WebView webView, JasperReportView.InflateCallback callback) {
        callback.onStartInflate();
    }

    @Override
    void afterTemplateLoaded(WebView webView, JasperReportView.InflateCallback callback) {
        callback.onFinishInflate();
    }
}
