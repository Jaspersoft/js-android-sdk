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
import com.jaspersoft.android.sdk.widget.report.view.ReportView;
import com.jaspersoft.android.sdk.widget.report.view.ReportViewEventListener;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity extends AppCompatActivity implements ReportViewEventListener {
    private Resource resource;
    private ReportView reportView;
    private AuthorizedClient authorizedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_renderer_preview);
        reportView = (ReportView) findViewById(R.id.reportView);

        Bundle extras = getIntent().getExtras();
        resource = extras.getParcelable(ResourcesActivity.RESOURCE_EXTRA);

        Provider<AuthorizedClient> clientProvider = new ClientProvider(this, resource.getProfile());
        authorizedClient = clientProvider.provide();

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setEdition("PRO");
        serverInfo.setVersion(ServerVersion.v6_0_1);

        reportView.init(authorizedClient, serverInfo);
        reportView.setReportViewEventListener(this);
        reportView.run(new RunOptions.Builder()
                .reportUri(resource.getUri())
                .build());
    }

    @Override
    public void onHyperlinkClicked(Hyperlink hyperlink) {
//        if (hyperlink instanceof LocalHyperlink) {
//            reportRenderer.navigateTo(((LocalHyperlink) hyperlink).getDestination());
//        } else if (hyperlink instanceof ReferenceHyperlink) {
//            Toast.makeText(this, ((ReferenceHyperlink) hyperlink).getReference().toString(), Toast.LENGTH_SHORT).show();
//        } else if (hyperlink instanceof ReportExecutionHyperlink) {
//            RunOptions runOptions = ((ReportExecutionHyperlink) hyperlink).getRunOptions();
//            reportRenderer.render(runOptions);
//        }
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
            reportView.run(new RunOptions.Builder()
                    .reportUri(resource.getUri())
                    .build());
        }
    }
}
