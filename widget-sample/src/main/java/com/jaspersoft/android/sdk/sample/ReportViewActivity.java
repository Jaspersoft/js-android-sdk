package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.jaspersoft.android.sdk.widget.report.ReportClient;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity extends ResourceActivity {
    private TextView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = (TextView) findViewById(R.id.progress);
    }

    @Override
    public void onWebViewReady(WebView webView) {
        ReportClient container = new ReportClient.Builder(provideClient(), webView).build();
        container.init(new ReportClient.InflateCallback() {
            @Override
            public void onStartInflate() {
                progress.setText("Start inflate");
            }

            @Override
            public void onFinishInflate() {
                progress.setText("Finish inflate");
            }
        });
    }
}
