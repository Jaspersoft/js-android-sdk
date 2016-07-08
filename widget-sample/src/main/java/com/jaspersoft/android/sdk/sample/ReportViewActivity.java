package com.jaspersoft.android.sdk.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.ReferenceHyperlink;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.ReportExecutionHyperlink;
import com.jaspersoft.android.sdk.widget.report.view.PaginationView;
import com.jaspersoft.android.sdk.widget.report.view.ReportFragment;
import com.jaspersoft.android.sdk.widget.report.view.ReportFragmentEventListener;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity extends AppCompatActivity implements ReportFragmentEventListener {
    private Resource resource;
    private ReportFragment reportFragment;
    private AuthorizedClient authorizedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_renderer_preview);
        reportFragment = (ReportFragment) getSupportFragmentManager().findFragmentById(R.id.reportFragment);
        PaginationView paginationView = (PaginationView) findViewById(R.id.pagination);
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportFragment.refresh();
            }
        });

        Bundle extras = getIntent().getExtras();
        resource = extras.getParcelable(ResourcesActivity.RESOURCE_EXTRA);

        Provider<AuthorizedClient> clientProvider = new ClientProvider(this, resource.getProfile());
        authorizedClient = clientProvider.provide();

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setEdition("PRO");
        serverInfo.setVersion(ServerVersion.v5_6_1);

        reportFragment.setReportFragmentEventListener(this);
        reportFragment.setPaginationView(paginationView);
        if (!reportFragment.isInited()) {
            reportFragment.init(authorizedClient, serverInfo, 0.5f);
            reportFragment.run(new RunOptions.Builder()
                    .reportUri(resource.getUri())
                    .build());
        }
    }

    @Override
    public void onActionsAvailabilityChanged() {

    }

    @Override
    public void onHyperlinkClicked(Hyperlink hyperlink) {
        if (hyperlink instanceof ReferenceHyperlink) {
            Toast.makeText(this, ((ReferenceHyperlink) hyperlink).getReference().toString(), Toast.LENGTH_SHORT).show();
        } else if (hyperlink instanceof ReportExecutionHyperlink) {
            RunOptions runOptions = ((ReportExecutionHyperlink) hyperlink).getRunOptions();
            reportFragment.run(runOptions);
        }
    }

    @Override
    public void onError(ServiceException exception) {
        if (exception.code() == StatusCodes.AUTHORIZATION_ERROR) {
            new AuthTask().execute();
        }
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
