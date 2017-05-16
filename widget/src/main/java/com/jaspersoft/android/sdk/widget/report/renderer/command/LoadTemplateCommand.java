package com.jaspersoft.android.sdk.widget.report.renderer.command;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.renderer.AssetFile;
import com.jaspersoft.android.sdk.widget.report.renderer.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.renderer.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class LoadTemplateCommand extends Command {
    private final WebView webView;
    private final String baseUrl;
    private final String restTemplate;

    LoadTemplateCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, String baseUrl, String restTemplate) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
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
                webView.setWebChromeClient(new WebViewProgressListener());
            }

            @Override
            protected String doInBackground(Object... params) {
                AssetFile assetFile = assetFactory.load(restTemplate);
                return assetFile.toString();
            }

            @Override
            protected void onPostExecute(String template) {
                webView.loadDataWithBaseURL(baseUrl, template, "text/html", "utf-8", null);
            }
        };
    }

    private class WebViewProgressListener extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress == 100) {
                view.setWebChromeClient(null);
                dispatcher.dispatch(eventFactory.createTemplateLoadedEvent());
            }
        }
    }
}
