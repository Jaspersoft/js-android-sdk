package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaspersoft.android.sdk.widget.dashboard.DashboardClient;
import com.jaspersoft.android.sdk.widget.dashboard.DashboardHyperlink;
import com.jaspersoft.android.sdk.widget.RetainedWebViewFragment;
import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.WindowError;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class DashboardViewActivity extends ResourceActivity implements RetainedWebViewFragment.Callback {

    public static final String RESOURCE_VIEW_KEY = "resource-view";

    private DashboardClient resourceView;
    private TextView progress;
    private Bundle savedState;
    private WebView webView;

    private boolean maximized;

    public void onCreate(Bundle in) {
        super.onCreate(in);
        savedState = in;
        progress = (TextView) findViewById(R.id.progress);
        progress.setText("Loading...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (maximized) {
                    minimizeDashlet();
                } else {
                    onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void minimizeDashlet() {
        resourceView.minimizeDashlet(webView);
    }

    @Override
    public void onWebViewReady(WebView webView) {
        resourceView = provideDashboardView(savedState)
                .registerErrorCallbacks(new DashboardClient.ErrorCallbacks() {
                    @Override
                    public void onWindowError(WindowError error) {
                        progress.setText(error.toString());
                    }
                })
                .registerDashletCallbacks(new DashboardClient.DashletCallbacks() {
                    @Override
                    public void onMaximizeStart(String componentName) {
                        progress.setText("Start maximizing component: " + componentName);
                        showMinimizeControl();
                    }

                    @Override
                    public void onMaximizeEnd(String componentName) {
                        progress.setText("End maximizing component: " + componentName);
                    }

                    @Override
                    public void onMinimizeStart(String componentName) {
                        progress.setText("Start minimizing component: " + componentName);
                        hideMinimizeControl();
                    }

                    @Override
                    public void onMinimizeEnd(String componentName) {
                        progress.setText("End minimizing component: " + componentName);
                    }

                    @Override
                    public void onHypeLinkClick(DashboardHyperlink dashboardHyperlink) {
                        Toast.makeText(DashboardViewActivity.this, dashboardHyperlink.getType().toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .registerLifecycleCallbacks(new DashboardClient.LifecycleCallbacks() {
                    @Override
                    public void onInflateFinish() {
                        progress.setText("Awaiting script...");
                    }

                    @Override
                    public void onScriptLoaded() {
                        progress.setText("Awaiting dashboard...");
                    }

                    @Override
                    public void onDashboardRendered() {
                        progress.setText("Dashboard rendered");
                    }
                });
        this.webView = webView;
        runDashboard();
    }

    private void showMinimizeControl() {
        maximized = true;
        getToolbar().setNavigationIcon(R.drawable.ic_close);
    }

    private void hideMinimizeControl() {
        maximized = false;
        getToolbar().setNavigationIcon(R.drawable.ic_home);
    }

    private void runDashboard() {
        RunOptions options = new RunOptions.Builder()
                .client(provideClient())
                .webView(webView)
                .uri(provideResource().getUri())
                .build();
        resourceView.run(options);
    }

    @Override
    public void onResume() {
        super.onResume();
        resourceView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        resourceView.pause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RESOURCE_VIEW_KEY, resourceView);
    }

    private DashboardClient provideDashboardView(Bundle in) {
        DashboardClient reportView;
        if (in == null) {
            reportView = new DashboardClient();
        } else {
            reportView = in.getParcelable(RESOURCE_VIEW_KEY);
        }
        return reportView;
    }
}
