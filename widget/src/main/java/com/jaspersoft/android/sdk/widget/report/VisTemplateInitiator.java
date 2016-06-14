package com.jaspersoft.android.sdk.widget.report;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class VisTemplateInitiator extends TemplateInitiator {
    private static final String INIT_COMMAND_SCRIPT = "javascript:MobileReport.getInstance().init({server: {url: \"%s\"}})";

    VisTemplateInitiator(AuthorizedClient client) {
        super(client, "report-vis-template.html");
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void beforeTemplateLoaded(final WebView webView, final ReportClient.InflateCallback callback) {
        callback.onStartInflate();
        WebInterface webInterface = new WebInterface(webView, callback);
        webView.addJavascriptInterface(webInterface, "Android");
    }

    @Override
    void afterTemplateLoaded(WebView webView, ReportClient.InflateCallback callback) {
        webView.loadUrl(String.format(INIT_COMMAND_SCRIPT, getClient().getBaseUrl()));
    }

    private class WebInterface {
        private final WebView webView;
        private final ReportClient.InflateCallback callback;

        private WebInterface(WebView webView, ReportClient.InflateCallback callback) {
            this.webView = webView;
            this.callback = callback;
        }

        @JavascriptInterface
        public void onVisualizeReady() {
            runOnUiThread(webView, new Runnable() {
                @Override
                public void run() {
                    callback.onFinishInflate();
                }
            });
        }
    }
}
