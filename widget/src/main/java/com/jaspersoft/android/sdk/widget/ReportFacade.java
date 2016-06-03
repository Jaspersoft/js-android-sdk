package com.jaspersoft.android.sdk.widget;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;


/**
 * @author Tom Koptel
 * @since 2.5
 */
class ReportFacade {
    private final WebView webView;
    private final TaskExecutor taskExecutor;
    private final AuthorizedClient client;
    private final TemplateInitiator.Factory templateFactory;

    ReportFacade(Builder builder) {
        this.client = builder.client;
        this.taskExecutor = builder.taskExecutor;
        this.webView = builder.webView;
        this.templateFactory = builder.templateFactory;
    }

    public void init(JasperReportView.InflateCallback callback) {
        if (callback == null) {
            callback = JasperReportView.InflateCallback.NULL;
        }
        final JasperReportView.InflateCallback finalCallback = callback;
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

        public ReportFacade build() {
            taskExecutor = AsyncTaskExecutor.INSTANCE;
            templateFactory = new TemplateInitiator.Factory();
            return new ReportFacade(this);
        }
    }
}
