package com.jaspersoft.android.sdk.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.sample.page.ReportMessageFactory;
import com.jaspersoft.android.sdk.sample.page.ReportPage;
import com.jaspersoft.android.sdk.widget.RunOptions;
import com.jaspersoft.android.sdk.widget.WindowError;
import com.jaspersoft.android.sdk.widget.report.v1.ReportClient;

import java.util.List;

import static com.jaspersoft.android.sdk.sample.DashboardViewActivity.RESOURCE_VIEW_KEY;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class ReportViewActivity extends ResourceActivity implements ParamsDialog.OnResult {
    static final String PAGE_STATE_KEY = "page-state";
    static final String DIALOG_STATE_KEY = "params-dialog";

    private TextView progress;
    private ReportClient reportClient;
    private ReportPage pageState;
    private WebView webView;
    private ParamsDialog paramsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        paramsDialog = new ParamsDialog.Builder(this, savedInstanceState)
                .params(provideResource().getParams())
                .build();
        paramsDialog.setCallback(this);

        progress = (TextView) findViewById(R.id.progress);
        reportClient = provideReportClient(savedInstanceState);
        pageState = provideState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RESOURCE_VIEW_KEY, reportClient);
        outState.putParcelable(PAGE_STATE_KEY, pageState);
        outState.putBundle(DIALOG_STATE_KEY, paramsDialog.saveInstanceState());
    }

    private ReportPage provideState(Bundle in) {
        if (in == null) {
            return new ReportPage();
        }
        return in.getParcelable(PAGE_STATE_KEY);
    }

    private ReportClient provideReportClient(Bundle in) {
        if (in == null) {
            return new ReportClient();
        }
        return in.getParcelable(RESOURCE_VIEW_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controls, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.set_params:
                showParamsDialog();
                break;
            case R.id.run:
                runReport(webView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showParamsDialog() {
        paramsDialog.show();
    }

    @Override
    public void onWebViewReady(WebView webView) {
        this.webView = webView;
        reportClient.registerErrorCallbacks(new ReportClient.SimpleErrorCallbacks() {
            @Override
            public void onWindowError(WindowError error) {
                progress.setText("Window error: " + error.getMessage());
            }
        })
                .registerLifecycleCallbacks(new ReportClient.LifecycleCallbacks() {
                    @Override
                    public void onInflateFinish() {
                        updateState();
                        updateProgress();
                    }

                    @Override
                    public void onScriptLoaded() {
                        updateState();
                        updateProgress();
                    }

                    @Override
                    public void onReportRendered() {
                        updateState();
                        updateProgress();
                    }
                });

        if (pageState.loadingInProgress()) {
            updateProgress();
        } else {
            runReport(webView);
        }
    }

    private void runReport(WebView webView) {
        resetPage();
        updateProgress();

        RunOptions runOptions = new RunOptions.Builder()
                .client(provideClient())
                .uri(provideResource().getUri())
                .webView(webView)
                .build();
        reportClient.run(runOptions);
    }

    @Override
    public void onParamsResult(List<ReportParameter> params) {

    }

    private void updateState() {
        pageState.moveToNextState();
    }

    private void resetPage() {
        pageState.resetState();
    }

    private void updateProgress() {
        progress.setText(ReportMessageFactory.INSTANCE.messageFromState(pageState));
    }

    @Override
    protected void onPause() {
        super.onPause();
        reportClient.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reportClient.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        reportClient.removeCallbacks();
        paramsDialog.removeCallbacks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            reportClient.destroy();
        }
    }
}
