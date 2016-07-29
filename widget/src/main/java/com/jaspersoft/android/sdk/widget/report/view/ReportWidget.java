package com.jaspersoft.android.sdk.widget.report.view;

import android.view.View;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportWidget {

    void init(AuthorizedClient client, ServerInfo serverInfo, double scale);

    boolean isInited();

    boolean isControlActionsAvailable();

    ReportProperties getReportProperties();

    void setReportEventListener(ReportEventListener reportEventListener);

    void setReportPaginationListener(ReportPaginationListener reportPaginationLsitener);

    void setReportBookmarkListener(ReportBookmarkListener reportBookmarkListener);

    void setReportPartsListener(ReportPartsListener reportPartsListener);

    View getView();

    void run(RunOptions runOptions);

    void applyParams(List<ReportParameter> reportParameters);

    void refresh();

    void navigateToPage(int page);

    void performViewAction(ViewAction viewAction);

    void reset();
}
