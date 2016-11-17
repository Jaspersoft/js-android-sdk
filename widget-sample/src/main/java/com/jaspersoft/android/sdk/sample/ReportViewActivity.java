package com.jaspersoft.android.sdk.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.sample.di.ClientProvider;
import com.jaspersoft.android.sdk.sample.di.Provider;
import com.jaspersoft.android.sdk.sample.entity.Resource;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.view.ReportEventListener;
import com.jaspersoft.android.sdk.widget.report.view.ReportFragment;
import com.jaspersoft.android.sdk.widget.report.view.ReportPaginationListener;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity extends AppCompatActivity implements ReportEventListener, ReportPaginationListener {
    private Resource resource;
    private ReportFragment reportFragment;
    private AuthorizedClient authorizedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_renderer_preview);
        reportFragment = (ReportFragment) getSupportFragmentManager().findFragmentById(R.id.reportFragment);

        Bundle extras = getIntent().getExtras();
        resource = extras.getParcelable(ResourcesActivity.RESOURCE_EXTRA);

        Provider<AuthorizedClient> clientProvider = new ClientProvider(this, resource.getProfile());
        authorizedClient = clientProvider.provide();

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setEdition("PRO");
        serverInfo.setVersion(ServerVersion.v6_2);

        reportFragment.setReportEventListener(this);
        reportFragment.setReportPaginationListener(this);
        if (!reportFragment.isInited()) {
            reportFragment.init(authorizedClient, serverInfo, 0.5f);
            reportFragment.run(new RunOptions.Builder()
                    .reportUri(resource.getUri())
                    .build());
        }
    }

    @Override
    public void onActionAvailabilityChanged(ActionType actionType, boolean isAvailable) {

    }

    @Override
    public void onHyperlinkClicked(Hyperlink hyperlink) {
    }

    @Override
    public void onExternalLinkOpened(String url) {

    }

    @Override
    public void onError(ServiceException exception) {
        if (exception.code() == StatusCodes.AUTHORIZATION_ERROR) {
            new AuthTask().execute();
        }
    }

    @Override
    public void onPagesCountChanged(Integer totalPages) {

    }

    @Override
    public void onCurrentPageChanged(int currentPage) {

    }

    @Override
    public void onMultiPageStateChange(boolean isMultiPage) {

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
            reportFragment.run(new RunOptions.Builder()
                    .reportUri(resource.getUri())
                    .build());
        }
    }
}
