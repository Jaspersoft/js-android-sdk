package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportClient {
    private final AuthorizedClient client;
    private final PresenterCache presenterCache;

    public ReportClient(AuthorizedClient client) {
        this(client, PresenterCache.getInstance());
    }

    ReportClient(AuthorizedClient client, PresenterCache presenterCache) {
        this.client = client;
        this.presenterCache = presenterCache;
    }

    public ReportPresenter newPresenter(String uri, WebView webView) {
        ReportListeners listeners = new ReportListeners();
        PresenterKey key = PresenterKey.newKey();
        ReportPresenter reportPresenter = new ReportPresenter(
                this, listeners, key, uri, webView
        );
        presenterCache.put(reportPresenter);
        return reportPresenter;
    }

    public ReportPresenter getRunningInstance(PresenterKey key) {
        return presenterCache.get(key);
    }

    void removePresenter(ReportPresenter presenter) {
        presenterCache.remove(presenter.getKey());
    }

    AuthorizedClient getApiClient() {
        return client;
    }
}
