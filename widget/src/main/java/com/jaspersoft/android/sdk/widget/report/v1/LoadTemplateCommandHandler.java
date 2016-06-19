package com.jaspersoft.android.sdk.widget.report.v1;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.internal.AssetFile;
import com.jaspersoft.android.sdk.widget.internal.Dispatcher;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class LoadTemplateCommandHandler implements CommandHandler<LoadTemplateCommand> {
    private final Dispatcher dispatcher;
    private final Event.Factory eventFactory;
    private final Command.Factory commandFactory;
    private final Object javascriptInterface;
    private final String templateName;
    private final double version;

    private AsyncTask task = DummyAsyncTask.INSTANCE;

    LoadTemplateCommandHandler(
            Dispatcher dispatcher,
            Event.Factory eventFactory,
            Command.Factory commandFactory,
            Object javascriptInterface,
            String templateName,
            double version
    ) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
        this.javascriptInterface = javascriptInterface;
        this.templateName = templateName;
        this.version = version;
    }

    @Override
    public void handle(LoadTemplateCommand command) {
        task = createTask(command.getOptions());
        AsyncTaskCompat.executeParallel(task);
    }

    @Override
    public void cancel() {
        task.cancel(true);
    }

    private Task createTask(RunOptions options) {
        WebView webView = options.getWebView();
        AuthorizedClient client = options.getClient();
        String baseUrl = client.getBaseUrl();
        AssetFile.Factory factory = new AssetFile.Factory(webView.getContext());
        return new Task(factory, webView, options, baseUrl);
    }

    private class Task extends AsyncTask<Object, Object, String> {
        private final AssetFile.Factory assetFactory;
        private final WebView webView;
        private final RunOptions options;
        private final String baseUrl;

        private Task(AssetFile.Factory factory, WebView webView, RunOptions options, String baseUrl) {
            this.assetFactory = factory;
            this.webView = webView;
            this.options = options;
            this.baseUrl = baseUrl;
        }

        @SuppressLint("JavascriptInterface")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            webView.addJavascriptInterface(javascriptInterface, "Android");
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);

                    if (newProgress == 100) {
                        webView.setWebChromeClient(null);
                        dispatcher.dispatch(eventFactory.createInflateCompleteEvent());
                        dispatcher.dispatch(commandFactory.createRunCommand(options, version));
                    }
                }
            });
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
    }
}
