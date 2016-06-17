package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportPresenter {
    private final ReportClient reportClient;
    private final ReportListeners listeners;
    private final PresenterKey presenterKey;
    private final String uri;
    private final WebView webView;
    private final Context context;

    private PresenterState state;

    ReportPresenter(
            ReportClient reportClient,
            ReportListeners listeners,
            PresenterKey presenterKey,
            String uri,
            WebView webView
    ) {
        this.reportClient = reportClient;
        this.listeners = listeners;
        this.presenterKey = presenterKey;
        this.uri = uri;
        this.webView = webView;
        this.context = new Context();
        this.state = new InitState(context);
    }

    public PresenterKey getKey() {
        return presenterKey;
    }

    public ReportPresenter registerProgressListener(ProgressListener progressListener) {
        listeners.setProgressListener(progressListener);
        return this;
    }

    public ReportPresenter registerHyperlinkClickListener(HyperlinkClickListener hyperlinkClickListener) {
        listeners.setHyperlinkClickListener(hyperlinkClickListener);
        return this;
    }

    public ReportPresenter registerErrorListener(ErrorListener errorListener) {
        listeners.setErrorListener(errorListener);
        return this;
    }

    public void run(RunOptions options) {
        state.run(options);
    }

    public void update(List<ReportParameter> parameters) {
        state.update(parameters);
    }

    public boolean isRunning() {
        return state.isRunning();
    }

    public void navigate(ReportQuery query) {
        state.navigate(query);
    }

    public void refresh() {
        state.refresh();
    }

    public void destroy() {
        reportClient.removePresenter(this);
        state.destroy();
    }

    public void removeCallbacks() {
        listeners.reset();
    }

    public interface ProgressListener {
    }

    public interface HyperlinkClickListener {
    }

    public interface ErrorListener {
    }

    public interface PropertyCallback<P> {
        void onResult(P property);
    }

    private class Context implements PresenterState.Context {
        @Override
        public WebView getWebView() {
            return webView;
        }

        @Override
        public String getUri() {
            return uri;
        }

        @Override
        public AuthorizedClient getClient() {
            return reportClient.getApiClient();
        }

        @Override
        public void swapState(PresenterState newState) {
            state = newState;
        }
    }
}
