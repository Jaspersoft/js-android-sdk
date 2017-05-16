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
 * Common interface for UI component that can render report
 *
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportWidget {

    /**
     * Initialize {@link ReportWidget} with provided params.
     * Should be called before performing any actions with {@link ReportWidget}.
     * @param client configured client
     * @param serverInfo base information about server
     * @param scale desirable scale for report rendering
     */
    void init(AuthorizedClient client, ServerInfo serverInfo, double scale);

    /**
     * Define {@link ReportWidget} initialization state
     * @return true if {@link ReportWidget} was already initialized, false - otherwise
     */
    boolean isInited();

    /**
     *  Define if {@link ReportWidget} is ready to perform next action.
     *  Only one action can be performed at one time.
     * @return true if any control actions is available, false - otherwise
     */
    boolean isControlActionsAvailable();

    /**
     * Get main report properties.
     * @return current report properties
     */
    ReportProperties getReportProperties();

    void setReportEventListener(ReportEventListener reportEventListener);

    void setReportPaginationListener(ReportPaginationListener reportPaginationListener);

    void setReportBookmarkListener(ReportBookmarkListener reportBookmarkListener);

    void setReportPartsListener(ReportPartsListener reportPartsListener);

    void setReportChartTypeListener(ReportChartTypeListener reportChartTypeListener);


    /**
     * Return view that is currently used for rendering report
     * @return view that is currently used for rendering report
     */
    View getView();

    /**
     * Start report rendering with initial render options
     * @param runOptions report render options
     */
    void run(RunOptions runOptions);

    /**
     * Rerun report with new report filters.
     * @param reportParameters report filters
     */
    void applyParams(List<ReportParameter> reportParameters);

    /**
     * Refresh report from scratch.
     * Report will be rendered with previous {@link RunOptions}.
     */
    void refresh();

    /**
     * Render specified page of multi page report
     * @param page desirable page
     */
    void navigateToPage(int page);

    /**
     * Perform view action on report like zoom or scroll.
     * Can be used instead or with touch control.
     * @param viewAction desired view action
     */
    void performViewAction(ViewAction viewAction);

    /**
     * Change chart type for chart report
     * @param component for changing chart type
     * @param newChartType desired chart type
     */
    void updateChartType(ReportComponent component, ChartType newChartType);

    /**
     * Clean report visualization. Return report view to initial state.
     */
    void reset();
}
