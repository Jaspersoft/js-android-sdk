package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.jaspersoft.android.sdk.cookie.CookieAuthenticationHandler;
import com.jaspersoft.android.sdk.cookie.CookieProvision;
import com.jaspersoft.android.sdk.network.AuthenticationLifecycle;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.widget.DashboardView;
import com.jaspersoft.android.sdk.widget.RetainedWebViewFragment;
import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.WindowError;

import java.net.CookieManager;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class DashboardViewActivity extends AppCompatActivity implements RetainedWebViewFragment.Callback{

    public static final String RESOURCE_VIEW_KEY = "resource-view";

    private DashboardView resourceView;
    private Resource resource;
    private Profile profile;
    private TextView progress;
    private Bundle savedState;
    private WebView webView;

    private boolean runControlFlag;

    public void onCreate(Bundle in) {
        super.onCreate(in);
        setContentView(R.layout.activity_preview);
        savedState = in;

        resource = extractResource();
        profile = resource.getProfile();

        progress = (TextView) findViewById(R.id.progress);
        progress.setText("Loading...");

        initWebView(in);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.run);
        item.setVisible(runControlFlag);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.controls, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.run:
                runDashboard();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWebViewReady(WebView webView) {
        setRunControlVisible();
        resourceView = provideDashboardView(savedState)
                .registerErrorCallback(new DashboardView.ErrorCallback() {
                    @Override
                    public void onWindowError(WindowError error) {
                        progress.setText(error.toString());
                    }
                })
                .registerLifecycle(new DashboardView.Lifecycle() {
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
    }

    private void setRunControlVisible() {
        runControlFlag = true;
        supportInvalidateOptionsMenu();
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

        CookieManager cookieManager = CookieProvision.provideHandler(this);
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
