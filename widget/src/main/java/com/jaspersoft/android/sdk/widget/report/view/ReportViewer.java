package com.jaspersoft.android.sdk.widget.report.view;

import android.view.View;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeature;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportViewer {
    void init(AuthorizedClient client, ServerInfo serverInfo);

    void init(AuthorizedClient client, ServerInfo serverInfo, float scale);

    boolean isInited();

    boolean isControlActionsAvailable();

    boolean isFeatureSupported(ReportFeature reportFeature);

    void run(RunOptions runOptions);

    void applyParams(List<ReportParameter> reportParameters);

    void refresh();

    void reset();

    void setReportEventListener(ReportEventListener reportEventListener);

    void setPaginationView(PaginationView paginationView);

    View getView();

    void performViewAction(ViewAction viewAction);
}
