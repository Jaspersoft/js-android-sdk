package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.sample.di.ClientProvider;
import com.jaspersoft.android.sdk.sample.di.Provider;
import com.jaspersoft.android.sdk.sample.entity.Resource;
import com.jaspersoft.android.sdk.widget.dashboard.RetainedWebViewFragment;

import static com.jaspersoft.android.sdk.sample.R.id.toolbar;


/**
 * @author Tom Koptel
 * @since 2.6
 */
public abstract class ResourceActivity extends AppCompatActivity implements RetainedWebViewFragment.Callback {
    private Resource resource;
    private Provider<AuthorizedClient> clientProvider;
    private Toolbar toolbarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        toolbarView = (Toolbar) findViewById(toolbar);
        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resource = extractResource();
        clientProvider = new ClientProvider(this, resource.getProfile());
        initWebView(savedInstanceState);
    }

    @Override
    public void onWebViewReady(WebView webView) {
    }

    protected final Toolbar getToolbar() {
        return toolbarView;
    }

    protected final AuthorizedClient provideClient() {
        return clientProvider.provide();
    }

    protected final Resource provideResource() {
        return resource;
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
