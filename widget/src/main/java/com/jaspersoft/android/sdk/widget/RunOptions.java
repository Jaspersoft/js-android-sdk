package com.jaspersoft.android.sdk.widget;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class RunOptions {
    private final AuthorizedClient client;
    private final WebView webView;
    private final String uri;

    private RunOptions(Builder builder) {
        this.client = builder.client;
        this.webView = builder.webView;
        this.uri = builder.uri;
    }

    public AuthorizedClient getClient() {
        return client;
    }

    public WebView getWebView() {
        return webView;
    }

    public String getUri() {
        return uri;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private AuthorizedClient client;
        private WebView webView;
        private String uri;

        public Builder() {
        }

        private Builder(RunOptions builder) {
            this.client = builder.client;
            this.webView = builder.webView;
            this.uri = builder.uri;
        }

        public Builder client(AuthorizedClient client) {
            this.client = client;
            return this;
        }

        public Builder webView(WebView webView) {
            this.webView = webView;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public RunOptions build() {
            return new RunOptions(this);
        }
    }
}
