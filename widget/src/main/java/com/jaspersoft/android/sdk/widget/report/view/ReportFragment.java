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
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeature;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportFragment extends Fragment implements ReportViewer {
    private ReportViewerDelegate reportViewerDelegate;
    private ReportRendererKey reportRendererKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportViewerDelegate = ReportViewerDelegate.create();

        if (savedInstanceState != null) {
            reportRendererKey = reportViewerDelegate.restoreData(savedInstanceState);
        }

        if (reportViewerDelegate.getResourceView() == null) {
            reportViewerDelegate.createResourceView(getContext());
        }
        reportViewerDelegate.getResourceView().setResourceWebViewEventListener(reportViewerDelegate);
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
        reportViewerDelegate.persistData(outState, reportRendererKey);
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
            reportViewerDelegate.destroy(reportRendererKey);
        }
    }

    @Override
    public void init(AuthorizedClient client, ServerInfo serverInfo) {
        init(client, serverInfo, 1f);
    }

    @Override
    public void init(AuthorizedClient client, ServerInfo serverInfo, float scale) {
        reportViewerDelegate.init(client, serverInfo, scale);
        reportRendererKey = reportViewerDelegate.saveInStore();
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
    public boolean isFeatureSupported(ReportFeature reportFeature) {
        return reportViewerDelegate.isFeatureSupported(reportFeature);
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
    public void reset() {
        reportViewerDelegate.reset();
    }

    @Override
    public void setReportEventListener(ReportEventListener reportEventListener) {
        reportViewerDelegate.setReportEventListener(reportEventListener);
    }

    @Override
    public void setPaginationView(PaginationView paginationView) {
        reportViewerDelegate.setPaginationView(paginationView);
    }

    @Override
    public void performViewAction(ViewAction viewAction) {
        reportViewerDelegate.performViewAction(viewAction);
    }
}
