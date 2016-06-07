package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.cookie.CookieAuthenticationHandler;
import com.jaspersoft.android.sdk.cookie.CookieProvision;
import com.jaspersoft.android.sdk.network.AuthenticationLifecycle;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.widget.DashboardView;
import com.jaspersoft.android.sdk.widget.RetainedWebViewFragment;

import java.net.CookieManager;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class DashboardViewActivity extends AppCompatActivity implements DashboardView.DashboardCallbacks {

    public static final String RESOURCE_VIEW_KEY = "resource-view";

    private DashboardView resourceView;
    private Resource resource;
    private Profile profile;

    public void onCreate(Bundle in) {
        super.onCreate(in);
        setContentView(R.layout.activity_preview);

        resource = extractResource();
        profile = resource.getProfile();

        WebView webView = provideWebview(in);
        resourceView = provideDashboardView(profile, in)
                .registerView(webView)
                .registerCallbacks(this)
                .done();

        resourceView.run(resource.getUri()); // if process already
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

    public void onSavedInstanceState(Bundle out) {
        out.putParcelable(RESOURCE_VIEW_KEY, resourceView);
    }

    private DashboardView provideDashboardView(Profile profile, Bundle in) {
        DashboardView reportView;
        if (in == null) {
            AuthorizedClient client = provideClient(profile);
            reportView = DashboardView.newInstance(client);
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

    private WebView provideWebview(Bundle in) {
        RetainedWebViewFragment webViewFragment;
        if (in == null) {
            webViewFragment = new RetainedWebViewFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, webViewFragment, "web-view")
                    .commitNow();
        } else {
            webViewFragment = (RetainedWebViewFragment) getSupportFragmentManager()
                    .findFragmentByTag("web-view");
        }
        return webViewFragment.getWebView();
    }

}
