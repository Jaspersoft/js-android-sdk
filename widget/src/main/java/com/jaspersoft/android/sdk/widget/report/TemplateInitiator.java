package com.jaspersoft.android.sdk.widget.report;

import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.auth.AuthorizationService;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import com.jaspersoft.android.sdk.widget.internal.AssetFile;

/**
 * @author Tom Koptel
 * @since 2.5
 */
abstract class TemplateInitiator {
    private final AuthorizedClient client;
    private final String templateName;

    protected TemplateInitiator(AuthorizedClient client, String templateName) {
        this.client = client;
        this.templateName = templateName;
    }

    AuthorizedClient getClient() {
        return client;
    }

    final void initiateTemplate(final WebView webView, final ReportClient.InflateCallback inflateCallback) {
        runOnUiThread(webView, new Runnable() {
            @Override
            public void run() {
                beforeTemplateLoaded(webView, inflateCallback);
            }
        });
        setupCallback(webView, inflateCallback);

        String baseUrl = client.getBaseUrl();

        AssetFile.Factory assetFactory = new AssetFile.Factory(webView.getContext());
        AssetFile assetFile = assetFactory.load(templateName);
        loadAsset(webView, baseUrl, assetFile.toString());
    }

    protected final void runOnUiThread(WebView webView, Runnable runnable) {
        ((Activity) webView.getContext()).runOnUiThread(runnable);
    }

    abstract void beforeTemplateLoaded(WebView webView, final ReportClient.InflateCallback callback);

    abstract void afterTemplateLoaded(WebView webView, ReportClient.InflateCallback callback);

    private void setupCallback(final WebView webView, final ReportClient.InflateCallback inflateCallback) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);

                        if (newProgress == 100) {
                            runOnUiThread(webView, new Runnable() {
                                @Override
                                public void run() {
                                    afterTemplateLoaded(webView, inflateCallback);
                                }
                            });
                            webView.setWebChromeClient(null);
                        }
                    }
                });
            }
        });
    }

    private void loadAsset(final WebView webView, final String baseUrl, final String content) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadDataWithBaseURL(baseUrl, content, "text/html", "utf-8", null);
            }
        });
    }

    static class Factory {
        TemplateInitiator provideTemplate(AuthorizedClient client) {
            try {
                AuthorizationService authorizationService = AuthorizationService.newService(client);
                authorizationService.authorize(client.getCredentials());

                ServerInfoService infoService = ServerInfoService.newService(client);
                ServerInfo serverInfo = infoService.requestServerInfo();

                ServerVersion version = serverInfo.getVersion();
                if (version.greaterThanOrEquals(ServerVersion.v6)) {
                    return new VisTemplateInitiator(client);
                } else {
                    return new RestTemplateInitiator(client);
                }
            } catch (ServiceException e) {
                // TODO handle network errors
                throw new RuntimeException(e);
            }
        }
    }
}
