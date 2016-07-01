package com.jaspersoft.android.sdk.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.sample.di.ClientProvider;
import com.jaspersoft.android.sdk.sample.di.Provider;
import com.jaspersoft.android.sdk.sample.entity.Resource;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.ResourceWebView;
import com.jaspersoft.android.sdk.widget.report.Destination;
import com.jaspersoft.android.sdk.widget.report.RenderState;
import com.jaspersoft.android.sdk.widget.report.ReportRenderer;
import com.jaspersoft.android.sdk.widget.report.ReportRendererCallback;
import com.jaspersoft.android.sdk.widget.report.ReportRendererKey;
import com.jaspersoft.android.sdk.widget.report.RunOptions;
import com.jaspersoft.android.sdk.widget.report.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.LocalHyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.ReferenceHyperlink;
import com.jaspersoft.android.sdk.widget.report.hyperlink.ReportExecutionHyperlink;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity extends AppCompatActivity implements ReportRendererCallback {
    private static final String RENDERER_KEY_ARG = "renderer_key";

    private TextView progress;
    private ResourceWebView webView;
    private ReportRenderer reportRenderer;
    private Resource resource;
    private AuthorizedClient authorizedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_renderer_preview);
        progress = (TextView) findViewById(R.id.progress);
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Destination destination = new Destination(5);
                reportRenderer.navigateTo(destination);
            }
        });

        Bundle extras = getIntent().getExtras();
        resource = extras.getParcelable(ResourcesActivity.RESOURCE_EXTRA);

        Provider<AuthorizedClient> clientProvider = new ClientProvider(this, resource.getProfile());
        authorizedClient = clientProvider.provide();

        if (savedInstanceState != null) {
            webView = ResourceWebViewStore.getInstance().getResourceWebView();
            ((ViewGroup) findViewById(R.id.container)).addView(webView);

            ReportRendererKey key = (ReportRendererKey) savedInstanceState.getSerializable(RENDERER_KEY_ARG);
            reportRenderer = ReportRenderer.restore(key);
        } else {
            webView = new ResourceWebView(this);
            ((ViewGroup) findViewById(R.id.container)).addView(webView);

            ServerInfo serverInfo = new ServerInfo();
            serverInfo.setEdition("PRO");
            serverInfo.setVersion(ServerVersion.v6_2);

            reportRenderer = ReportRenderer.create(authorizedClient, webView, serverInfo);
            reportRenderer.init(0.5f);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ReportRendererKey key = reportRenderer.persist();
        outState.putSerializable(RENDERER_KEY_ARG, key);

        ((ViewGroup) webView.getParent()).removeView(webView);
        ResourceWebViewStore.getInstance().setWebView(webView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reportRenderer.registerReportRendererCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reportRenderer.unregisterReportRendererCallback();
    }

    @Override
    public void onProgressStateChange(boolean inProgress) {
        webView.setVisibility(inProgress ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRenderStateChanged(RenderState renderState) {
        progress.setText(renderState.name());
        if (renderState == RenderState.INITED) {
            reportRenderer.run(new RunOptions.Builder()
                    .reportUri(resource.getUri())
                    .build());
        }
    }

    @Override
    public void onHyperlinkClicked(Hyperlink hyperlink) {
        if (hyperlink instanceof LocalHyperlink) {
            reportRenderer.navigateTo(((LocalHyperlink) hyperlink).getDestination());
        } else if (hyperlink instanceof ReferenceHyperlink) {
            Toast.makeText(this, ((ReferenceHyperlink) hyperlink).getReference().toString(), Toast.LENGTH_SHORT).show();
        } else if (hyperlink instanceof ReportExecutionHyperlink) {
            RunOptions runOptions = ((ReportExecutionHyperlink) hyperlink).getRunOptions();
            reportRenderer.run(runOptions);
        }
    }

    @Override
    public void onError(ServiceException exception) {
        progress.setText(exception.getMessage());
        if (exception.code() == StatusCodes.AUTHORIZATION_ERROR) {
            new AuthTask().execute();
        }
    }

    private double getScreenDiagonal() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        return Math.sqrt(x+y);
    }

    private class AuthTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                authorizedClient.repositoryApi().searchResources(null);
            } catch (IOException e) {
                return false;
            } catch (HttpException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean login) {
            reportRenderer.run(new RunOptions.Builder()
                    .reportUri(resource.getUri())
                    .build());
        }
    }

    private static class ResourceWebViewStore {
        private static ResourceWebViewStore resourceWebViewStore;

        private ResourceWebView webView;

        public static ResourceWebViewStore getResourceWebViewStore() {
            return resourceWebViewStore;
        }

        public static void setResourceWebViewStore(ResourceWebViewStore resourceWebViewStore) {
            ResourceWebViewStore.resourceWebViewStore = resourceWebViewStore;
        }

        public ResourceWebView getResourceWebView() {
            return webView;
        }

        public void setWebView(ResourceWebView webView) {
            this.webView = webView;
        }

        public static ResourceWebViewStore getInstance() {
            if (resourceWebViewStore == null) {
                resourceWebViewStore = new ResourceWebViewStore();
            }
            return resourceWebViewStore;
        }
    }
}
