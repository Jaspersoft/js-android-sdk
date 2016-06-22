package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.jaspersoft.android.sdk.cookie.CookieAuthenticationHandler;
import com.jaspersoft.android.sdk.cookie.RestCookieManager;
import com.jaspersoft.android.sdk.network.AuthenticationLifecycle;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.sample.entity.Resource;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.v3.RenderState;
import com.jaspersoft.android.sdk.widget.report.v3.ReportRendered;
import com.jaspersoft.android.sdk.widget.report.v3.ReportRendererCallback;
import com.jaspersoft.android.sdk.widget.report.v3.ReportRendererKey;

import java.net.CookieManager;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity3 extends AppCompatActivity implements ReportRendererCallback {
    private static final String RENDERER_KEY_ARG = "renderer_key";

    private TextView progress;
    private WebView webView;
    private ReportRendered reportRendered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_renderer_preview);
        progress = (TextView) findViewById(R.id.progress);

        Bundle extras = getIntent().getExtras();
        Resource resource = extras.getParcelable(ResourcesActivity.RESOURCE_EXTRA);

        if (savedInstanceState != null) {
            webView = WebViewStore.getInstance().getWebView();
            ((ViewGroup) findViewById(R.id.container)).addView(webView);

            ReportRendererKey key = (ReportRendererKey) savedInstanceState.getSerializable(RENDERER_KEY_ARG);
            reportRendered = new ReportRendered.Builder()
                    .withKey(key)
                    .build();
            reportRendered.init();
        } else {
            webView = new WebView(getApplicationContext());
            webView.getSettings().setJavaScriptEnabled(true);
            ((ViewGroup) findViewById(R.id.container)).addView(webView);

            reportRendered = new ReportRendered.Builder()
                    .withClient(provideClient(resource))
                    .withWebView(webView)
                    .build();
            reportRendered.init();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ReportRendererKey key = reportRendered.persist();
        outState.putSerializable(RENDERER_KEY_ARG, key);

        ((ViewGroup) webView.getParent()).removeView(webView);
        WebViewStore.getInstance().setWebView(webView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reportRendered.registerReportRendererCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reportRendered.unregisterReportRendererCallback();
    }

    @Override
    public void onRenderStateChanged(RenderState renderState) {
        progress.setText(renderState.name());
    }

    @Override
    public void onError(ServiceException exception) {

    }

    private AuthorizedClient provideClient(Resource resource) {
        Server server = Server.builder()
                .withBaseUrl(resource.getProfile().getUrl())
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

    private static class WebViewStore {
        private static WebViewStore webViewStore;

        private WebView webView;

        public static WebViewStore getWebViewStore() {
            return webViewStore;
        }

        public static void setWebViewStore(WebViewStore webViewStore) {
            WebViewStore.webViewStore = webViewStore;
        }

        public WebView getWebView() {
            return webView;
        }

        public void setWebView(WebView webView) {
            this.webView = webView;
        }

        public static WebViewStore getInstance() {
            if (webViewStore == null) {
                webViewStore = new WebViewStore();
            }
            return webViewStore;
        }
    }
}
