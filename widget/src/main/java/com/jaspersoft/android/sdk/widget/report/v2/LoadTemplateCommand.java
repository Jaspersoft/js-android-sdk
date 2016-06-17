package com.jaspersoft.android.sdk.widget.report.v2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.internal.AssetFile;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class LoadTemplateCommand extends Command {
    private final WebView webView;
    private final String baseUrl;
    private final String templateName;
    private final WebChromeClient progressListener;
    private final Object javascriptInterface;

    public LoadTemplateCommand(
            WebView webView,
            String baseUrl,
            String templateName,
            WebChromeClient progressListener,
            Object javascriptInterface
    ) {
        this.webView = webView;
        this.baseUrl = baseUrl;
        this.templateName = templateName;
        this.progressListener = progressListener;
        this.javascriptInterface = javascriptInterface;
    }

    @Override
    protected AsyncTask createTask() {
        return new AsyncTask<Object, Object, String>() {
            private AssetFile.Factory assetFactory;

            @SuppressLint("JavascriptInterface")
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                assetFactory = new AssetFile.Factory(webView.getContext());
                webView.addJavascriptInterface(javascriptInterface, "Android");
                webView.setWebChromeClient(progressListener);
            }

            @Override
            protected String doInBackground(Object... params) {
                AssetFile assetFile = assetFactory.load(templateName);
                return assetFile.toString();
            }

            @Override
            protected void onPostExecute(String template) {
                webView.loadDataWithBaseURL(baseUrl, template, "text/html", "utf-8", null);
            }
        };
    }
}
