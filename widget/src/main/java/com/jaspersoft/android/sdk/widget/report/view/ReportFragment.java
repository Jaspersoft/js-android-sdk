package com.jaspersoft.android.sdk.widget.report.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.widget.base.ResourceWebView;
import com.jaspersoft.android.sdk.widget.report.renderer.ChartType;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportComponent;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportFragment extends Fragment implements ReportWidget {
    private static final String RENDERER_KEY_ARG = "rendererKey";
    private static final String IN_PENDING_ARG = "inPendingKey";
    private static final String REPORT_PROPERTIES_ARG = "reportPropertiesKey";

    private ReportViewerDelegate reportViewerDelegate;
    private ReportRendererKey reportRendererKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            reportRendererKey = restoreState(savedInstanceState);
        } else {
            reportViewerDelegate = ReportViewerDelegate.create();
            reportRendererKey = ReportRendererKey.newKey();
        }

        if (reportViewerDelegate.getResourceView() == null) {
            reportViewerDelegate.createResourceView(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return reportViewerDelegate.getResourceView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!reportViewerDelegate.isInited()) {
            throw new RuntimeException("Report fragment must be inited before resuming!");
        }

        reportViewerDelegate.registerReportRendererCallback();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!reportViewerDelegate.isInited()) {
            reportViewerDelegate.unregisterReportRendererCallback();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reportViewerDelegate.removeResourceView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (reportViewerDelegate.isInited() && getActivity().isFinishing()) {
            reportViewerDelegate.destroy();
            destroyState();
        }
    }

    @Override
    public void init(AuthorizedClient client, ServerInfo serverInfo, double scale) {
        reportViewerDelegate.init(client, serverInfo, scale);
    }

    @Override
    public boolean isInited() {
        return reportViewerDelegate.isInited();
    }

    @Override
    public boolean isControlActionsAvailable() {
        return reportViewerDelegate.isControlActionsAvailable();
    }

    @Override
    public ReportProperties getReportProperties() {
        return reportViewerDelegate.getReportMetadata();
    }

    @Override
    public void run(RunOptions runOptions) {
        reportViewerDelegate.run(runOptions);
    }

    @Override
    public void applyParams(List<ReportParameter> reportParameters) {
        reportViewerDelegate.applyParams(reportParameters);
    }

    @Override
    public void refresh() {
        reportViewerDelegate.refresh();
    }

    @Override
    public void navigateToPage(int page) {
        reportViewerDelegate.navigateToPage(page);
    }

    @Override
    public void updateChartType(ReportComponent component, ChartType newChartType) {
        reportViewerDelegate.updateChartType(component, newChartType);
    }

    @Override
    public void reset() {
        reportViewerDelegate.reset();
    }

    @Override
    public void setReportEventListener(ReportEventListener reportEventListener) {
        reportViewerDelegate.setReportEventListener(reportEventListener);
    }

    @Override
    public void setReportPaginationListener(ReportPaginationListener reportPaginationLsitener) {
        reportViewerDelegate.setReportPaginationListener(reportPaginationLsitener);
    }

    @Override
    public void setReportBookmarkListener(ReportBookmarkListener reportBookmarkListener) {
        reportViewerDelegate.setReportBookmarkListener(reportBookmarkListener);
    }

    @Override
    public void setReportPartsListener(ReportPartsListener reportPartsListener) {
        reportViewerDelegate.setReportPartsListener(reportPartsListener);
    }

    @Override
    public void setReportChartTypeListener(ReportChartTypeListener reportChartTypeListener) {
        reportViewerDelegate.setReportChartTypeListener(reportChartTypeListener);
    }

    @Override
    public void performViewAction(ViewAction viewAction) {
        reportViewerDelegate.performViewAction(viewAction);
    }

    private void saveState(Bundle outState) {
        if (!reportViewerDelegate.isInited()) return;

        outState.putParcelable(RENDERER_KEY_ARG, reportRendererKey);
        outState.putBoolean(IN_PENDING_ARG, reportViewerDelegate.inPending);
        outState.putParcelable(REPORT_PROPERTIES_ARG, reportViewerDelegate.reportProperties);

        RenderersStore.getInstance().save(reportViewerDelegate.reportRenderer, reportRendererKey);
        ReportWebViewStore.getInstance().save(reportViewerDelegate.resourceWebView, reportRendererKey);
        RunOptionsStore.getInstance().save(reportViewerDelegate.runOptions, reportRendererKey);
    }

    private ReportRendererKey restoreState(Bundle state) {
        if (!state.containsKey(RENDERER_KEY_ARG)) return null;

        ReportRendererKey reportRendererKey = state.getParcelable(RENDERER_KEY_ARG);

        boolean inPending = state.getBoolean(IN_PENDING_ARG);
        ReportProperties reportProperties = state.getParcelable(REPORT_PROPERTIES_ARG);
        ReportRenderer reportRenderer = RenderersStore.getInstance().restore(reportRendererKey);
        ResourceWebView resourceWebView = ReportWebViewStore.getInstance().restore(reportRendererKey);
        RunOptions runOptions = RunOptionsStore.getInstance().restore(reportRendererKey);

        reportViewerDelegate = ReportViewerDelegate.restore(reportRenderer, resourceWebView, inPending, runOptions, reportProperties);

        return reportRendererKey;
    }

    private void destroyState() {
        if (reportRendererKey != null) {
            RenderersStore.getInstance().remove(reportRendererKey);
            ReportWebViewStore.getInstance().remove(reportRendererKey);
            RunOptionsStore.getInstance().remove(reportRendererKey);
        }
    }
}
