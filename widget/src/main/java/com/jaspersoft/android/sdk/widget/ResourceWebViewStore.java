package com.jaspersoft.android.sdk.widget;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ResourceWebViewStore {
    private static ResourceWebViewStore resourceWebViewStore;

    private ResourceWebView webView;

    public ResourceWebView getResourceWebView() {
        return webView;
    }

    public void setWebView(ResourceWebView webView) {
        this.webView = webView;
    }

    public static ResourceWebViewStore getInstance() {
        if (resourceWebViewStore == null) {
            resourceWebViewStore = new ResourceWebViewStore();
        }
        return resourceWebViewStore;
    }
}
