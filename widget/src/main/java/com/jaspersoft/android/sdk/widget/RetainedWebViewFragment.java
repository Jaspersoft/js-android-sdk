package com.jaspersoft.android.sdk.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class RetainedWebViewFragment extends Fragment {
    public static RetainedWebViewFragment newInstance() {
        RetainedWebViewFragment reportWebView = new RetainedWebViewFragment();
        reportWebView.setRetainInstance(true);
        return reportWebView;
    }

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (webView == null) {
            webView = new WebView(getActivity().getApplicationContext());
            webView.getSettings().setJavaScriptEnabled(true);
        }
        return webView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        boolean hasCallback = getActivity() instanceof Callback;
        if (hasCallback) {
            Callback callback = (Callback) getActivity();
            callback.onWebViewReady(webView);
        }
    }

    public WebView getWebView() {
        return webView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (getRetainInstance() && webView.getParent() instanceof ViewGroup) {
            ((ViewGroup) webView.getParent()).removeView(webView);
        }
    }

    public interface Callback {
        void onWebViewReady(WebView webView);
    }
}
