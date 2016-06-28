package com.jaspersoft.android.sdk.widget.report.command;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
abstract class InjectJsInterfaceCommand extends Command {

    private final WebView webView;

    protected InjectJsInterfaceCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView) {
        super(dispatcher, eventFactory);
        this.webView = webView;
    }

    @Override
    protected final AsyncTask createTask() {
        return new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                return null;
            }

            @SuppressLint("JavascriptInterface")
            @Override
            protected void onPostExecute(Void template) {
                webView.addJavascriptInterface(provideJsInterface(), "Android");
                dispatcher.dispatch(eventFactory.createJsInterfaceInjectEvent());
            }
        };
    }

    protected abstract JsInterface provideJsInterface();

    protected interface JsInterface{

    }
}
