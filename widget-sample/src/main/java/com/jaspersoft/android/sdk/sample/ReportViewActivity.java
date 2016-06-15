package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.report.ReportClient;

import static com.jaspersoft.android.sdk.sample.DashboardViewActivity.RESOURCE_VIEW_KEY;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity extends ResourceActivity {
    private TextView progress;
    private ReportClient reportClient;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = (TextView) findViewById(R.id.progress);
        reportClient = provideReportClient(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RESOURCE_VIEW_KEY, reportClient);
    }

    private ReportClient provideReportClient(Bundle in) {
        ReportClient client;
        if (in == null) {
            client = new ReportClient();
        } else {
            client = in.getParcelable(RESOURCE_VIEW_KEY);
        }
        return client;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controls, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.run:
                runReport(webView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWebViewReady(WebView webView) {
        this.webView = webView;
        reportClient.registerErrorCallbacks(new ReportClient.SimpleErrorCallbacks() {
                    @Override
                    public void onWindowError(WindowError error) {
                        progress.setText("Window error: " + error.getMessage());
                    }
                })
                .registerLifecycleCallbacks(new ReportClient.LifecycleCallbacks() {
                    @Override
                    public void onInflateFinish() {
                        progress.setText("Finish inflating template...");
                    }

                    @Override
                    public void onScriptLoaded() {
                        progress.setText("Finish script loading...");
                    }

                    @Override
                    public void onReportRendered() {
                        progress.setText("Finish report rendering...");
                    }
                });

        runReport(webView);
    }

    private void runReport(WebView webView) {
        progress.setText("Start loading report...");
        RunOptions runOptions = new RunOptions.Builder()
                .client(provideClient())
                .uri(provideResource().getUri())
                .webView(webView)
                .build();
        reportClient.run(runOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reportClient.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reportClient.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            reportClient.destroy();
        }
    }
}
