package com.jaspersoft.android.sdk.widget.report.command;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.widget.report.Dispatcher;
import com.jaspersoft.android.sdk.widget.report.event.EventFactory;
import com.jaspersoft.android.sdk.widget.report.jsinterface.JsInterface;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class InjectJsInterfaceCommand extends Command {
    private final WebView webView;
    private final JsInterface jsInterface;

    InjectJsInterfaceCommand(Dispatcher dispatcher, EventFactory eventFactory, WebView webView, JsInterface jsInterface) {
        super(dispatcher, eventFactory);
        this.webView = webView;
        this.jsInterface = jsInterface;
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
                webView.addJavascriptInterface(jsInterface, "Android");
                dispatcher.dispatch(eventFactory.createJsInterfaceInjectEvent());
            }
        };
    }
}
