package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jaspersoft.android.sdk.cookie.CookieAuthenticationHandler;
import com.jaspersoft.android.sdk.cookie.CookieProvision;
import com.jaspersoft.android.sdk.network.AuthenticationLifecycle;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.widget.JasperReportView;
import com.jaspersoft.android.sdk.widget.ReportViewFragment;

import java.net.CookieManager;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class PreviewActivity extends AppCompatActivity {
    private Button runControl;
    private ReportViewFragment reportViewFragment;
    private String url;
    private String uri;
    private TextView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        url = extras.getString(ProfilesActivity.SERVER_URL_EXTRA);
        uri = extras.getString(ProfilesActivity.REPORT_URI_EXTRA);

        setContentView(R.layout.activity_preview);
        setupWebView(savedInstanceState);

        progress = (TextView) findViewById(R.id.progress);
        runControl = (Button) findViewById(R.id.runControl);
        runControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newReport();
            }
        });
    }

    private void newReport() {
        reportViewFragment.init(provideClient(), new JasperReportView.InflateCallback() {
            @Override
            public void onStartInflate() {
                progress.setText("Start inflate");
            }

            @Override
            public void onFinishInflate() {
                progress.setText("Finish inflate");
            }
        });
    }

    private void setupWebView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            reportViewFragment = ReportViewFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, reportViewFragment, "web-view")
                    .commit();
        } else {
            reportViewFragment = (ReportViewFragment)
                    getSupportFragmentManager().findFragmentByTag("web-view");
        }
    }

    private AuthorizedClient provideClient() {
        Server server = Server.builder()
                .withBaseUrl(url)
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

}
