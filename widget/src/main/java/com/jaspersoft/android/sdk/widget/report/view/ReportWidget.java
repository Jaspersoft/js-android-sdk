package com.jaspersoft.android.sdk.widget.report.view;

import android.view.View;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.widget.report.renderer.ChartType;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportComponent;
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

    void setReportPaginationListener(ReportPaginationListener reportPaginationListener);

    void setReportBookmarkListener(ReportBookmarkListener reportBookmarkListener);

    void setReportPartsListener(ReportPartsListener reportPartsListener);

    void setReportChartTypeListener(ReportChartTypeListener reportChartTypeListener);

    View getView();

    void run(RunOptions runOptions);

    void applyParams(List<ReportParameter> reportParameters);

    void refresh();

    void navigateToPage(int page);

    void performViewAction(ViewAction viewAction);

    void updateChartType(ReportComponent component, ChartType newChartType);

    void reset();
}
