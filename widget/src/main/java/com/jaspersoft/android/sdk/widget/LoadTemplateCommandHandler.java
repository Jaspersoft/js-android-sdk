package com.jaspersoft.android.sdk.widget;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class LoadTemplateCommandHandler implements CommandHandler<LoadTemplateCommand> {
    private final Dispatcher dispatcher;
    private final Event.Factory eventFactory;
    private final Command.Factory commandFactory;

    LoadTemplateCommandHandler(
            Dispatcher dispatcher,
            Event.Factory eventFactory,
            Command.Factory commandFactory
    ) {
        this.dispatcher = dispatcher;
        this.eventFactory = eventFactory;
        this.commandFactory = commandFactory;
    }

    @Override
    public void handle(LoadTemplateCommand command) {
        RunOptions options = command.getOptions();
        WebView webView = options.getWebView();
        AuthorizedClient client = options.getClient();
        String baseUrl = client.getBaseUrl();


        AssetFile.Factory factory = new AssetFile.Factory(webView.getContext());
        Task task = new Task(factory, options, webView, baseUrl);
        AsyncTaskCompat.executeParallel(task);
    }

    private class Task extends AsyncTask<Void, Void, String> {
        private final AssetFile.Factory assetFactory;
        private final RunOptions options;
        private final WebView webView;
        private final String baseUrl;

        Task(AssetFile.Factory assetFactory, RunOptions options, WebView webView, String baseUrl) {
            this.webView = webView;
            this.baseUrl = baseUrl;
            this.options = options;
            this.assetFactory = assetFactory;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            webView.addJavascriptInterface(new VisualizeEvents(), "Android");
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);

                    if (newProgress == 100) {
                        webView.setWebChromeClient(null);
                        dispatcher.dispatch(eventFactory.createInflateCompleteEvent());
                        dispatcher.dispatch(commandFactory.createRunCommand(options));
                    }
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {
            AssetFile assetFile = assetFactory.load("dasboard-vis-template.html");
            return assetFile.toString();
        }

        @Override
        protected void onPostExecute(String template) {
            webView.loadDataWithBaseURL(baseUrl, template, "text/html", "utf-8", null);
        }
    }

    private class VisualizeEvents {
        @JavascriptInterface
        public void onVisualizeReady() {
            dispatcher.dispatch(eventFactory.createScriptLoadedEvent());
        }
    }
}
