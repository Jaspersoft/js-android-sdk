package com.jaspersoft.android.sdk.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * @author Tom Koptel
 * @since 2.5
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
            webView = new WebView(getActivity());
            webView.getSettings().setJavaScriptEnabled(true);
        }
        return webView;
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
}
