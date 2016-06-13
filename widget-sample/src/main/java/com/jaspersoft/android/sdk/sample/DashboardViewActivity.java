package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaspersoft.android.sdk.cookie.CookieAuthenticationHandler;
import com.jaspersoft.android.sdk.cookie.RestCookieManager;
import com.jaspersoft.android.sdk.network.AuthenticationLifecycle;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.widget.DashboardView;
import com.jaspersoft.android.sdk.widget.Hyperlink;
import com.jaspersoft.android.sdk.widget.RetainedWebViewFragment;
import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.WindowError;

import java.net.CookieManager;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class DashboardViewActivity extends AppCompatActivity implements RetainedWebViewFragment.Callback {

    public static final String RESOURCE_VIEW_KEY = "resource-view";

    private DashboardView resourceView;
    private Resource resource;
    private Profile profile;
    private TextView progress;
    private Bundle savedState;
    private WebView webView;

    private boolean maximized;
    private Toolbar toolbar;

    public void onCreate(Bundle in) {
        super.onCreate(in);
        setContentView(R.layout.activity_preview);
        savedState = in;

        resource = extractResource();
        profile = resource.getProfile();

        progress = (TextView) findViewById(R.id.progress);
        progress.setText("Loading...");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initWebView(in);
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
                .registerErrorCallbacks(new DashboardView.ErrorCallbacks() {
                    @Override
                    public void onWindowError(WindowError error) {
                        progress.setText(error.toString());
                    }
                })
                .registerDashletCallbacks(new DashboardView.DashletCallbacks() {
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
                    public void onHypeLinkClick(Hyperlink hyperlink) {
                        Toast.makeText(DashboardViewActivity.this, hyperlink.getType().toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .registerLifecycleCallbacks(new DashboardView.LifecycleCallbacks() {
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
        toolbar.setNavigationIcon(R.drawable.ic_close);
    }

    private void hideMinimizeControl() {
        maximized = false;
        toolbar.setNavigationIcon(R.drawable.ic_home);
    }

    private void runDashboard() {
        RunOptions options = new RunOptions.Builder()
                .client(provideClient(profile))
                .webView(webView)
                .uri(resource.getUri())
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

    private DashboardView provideDashboardView(Bundle in) {
        DashboardView reportView;
        if (in == null) {
            reportView = new DashboardView();
        } else {
            reportView = in.getParcelable(RESOURCE_VIEW_KEY);
        }
        return reportView;
    }

    private AuthorizedClient provideClient(Profile profile) {
        Server server = Server.builder()
                .withBaseUrl(profile.getUrl())
                .build();
        Credentials credentials = SpringCredentials.builder()
                .withPassword("superuser")
                .withUsername("superuser")
                .build();

        CookieManager cookieManager = new RestCookieManager.Builder(this)
                .handleWebViewCookies(false)
                .build();
        AuthenticationLifecycle lifecycle = new CookieAuthenticationHandler(cookieManager);

        return server.newClient(credentials)
                .withCookieHandler(cookieManager)
                .withAuthenticationLifecycle(lifecycle)
                .create();
    }

    private Resource extractResource() {
        Bundle extras = getIntent().getExtras();
        return extras.getParcelable(ResourcesActivity.RESOURCE_EXTRA);
    }

    private void initWebView(Bundle in) {
        if (in == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, RetainedWebViewFragment.newInstance(), "web-view")
                    .commit();
        }
    }
}
