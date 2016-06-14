package com.jaspersoft.android.sdk.widget.report;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;


/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportClient {
    private final WebView webView;
    private final TaskExecutor taskExecutor;
    private final AuthorizedClient client;
    private final TemplateInitiator.Factory templateFactory;

    ReportClient(Builder builder) {
        this.client = builder.client;
        this.taskExecutor = builder.taskExecutor;
        this.webView = builder.webView;
        this.templateFactory = builder.templateFactory;
    }

    public void init(InflateCallback callback) {
        if (callback == null) {
            callback = InflateCallback.NULL;
        }
        final InflateCallback finalCallback = callback;
        taskExecutor.perform(new Task() {
            @Override
            public void execute() {
                TemplateInitiator reportTemplate = templateFactory.provideTemplate(client);
                reportTemplate.initiateTemplate(webView, finalCallback);
            }
        });
    }

    public static class Builder {
        private final AuthorizedClient client;
        private final WebView webView;

        private TemplateInitiator.Factory templateFactory;
        private TaskExecutor taskExecutor;

        public Builder(AuthorizedClient client, WebView webView) {
            this.client = client;
            this.webView = webView;
        }

        public ReportClient build() {
            taskExecutor = AsyncTaskExecutor.INSTANCE;
            templateFactory = new TemplateInitiator.Factory();
            return new ReportClient(this);
        }
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
