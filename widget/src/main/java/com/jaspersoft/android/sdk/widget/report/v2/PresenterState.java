package com.jaspersoft.android.sdk.widget.report.v2;

import android.webkit.WebView;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.6
 */
abstract class PresenterState {
    private final Context context;

    protected PresenterState(Context context) {
        this.context = context;
    }

    public abstract void run(RunOptions options);

    public abstract void update(List<ReportParameter> parameters);

    public abstract boolean isRunning();

    public abstract void navigate(ReportQuery query);

    public abstract void refresh();

    public abstract void destroy();

    protected interface Context {
         WebView getWebView();
        String getUri();
        AuthorizedClient getClient();
        void swapState(PresenterState state);
    }
}
