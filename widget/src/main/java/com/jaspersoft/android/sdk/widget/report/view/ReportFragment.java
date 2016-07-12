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
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.ResourceWebView;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRendererCallback;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeature;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.LocalHyperlink;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportFragment extends Fragment implements PaginationView.PaginationListener {
    private static final String RENDERER_KEY_ARG = "rendererKey";
    private static final String SCALE_ARG = "scaleKey";
    private static final String IN_PENDING_ARG = "inPendingKey";

    private ReportRenderer reportRenderer;
    private ResourceWebView resourceWebView;
    private PaginationView paginationView;
    private ReportFragmentEventListener reportFragmentEventListener;

    private float scale;
    private boolean inPending;
    private RunOptions runOptions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(RENDERER_KEY_ARG)) {
            scale = savedInstanceState.getFloat(SCALE_ARG);
            inPending = savedInstanceState.getBoolean(IN_PENDING_ARG);
            ReportRendererKey reportRendererKey = savedInstanceState.getParcelable(RENDERER_KEY_ARG);

            reportRenderer = RenderersStore.INSTANCE.restoreExecutor(reportRendererKey);
            resourceWebView = ReportWebViewStore.INSTANCE.restoreReportView(reportRendererKey);
            runOptions = RunOptionsStore.INSTANCE.restoreRunOptions(reportRendererKey);
        }

        if (resourceWebView == null) {
            resourceWebView = new ResourceWebView(getContext().getApplicationContext());
        }

        setReportFragmentEventListener(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return resourceWebView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (reportRenderer == null) {
            throw new RuntimeException("Report fragment must be inited before resuming!");
        }

        reportRenderer.registerReportRendererCallback(new RendererEventListener());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ReportRendererKey reportRendererKey = RenderersStore.INSTANCE.saveExecutor(reportRenderer);
        ReportWebViewStore.INSTANCE.saveReportView(resourceWebView, reportRendererKey);
        RunOptionsStore.INSTANCE.saveRunOptions(runOptions, reportRendererKey);

        outState.putParcelable(RENDERER_KEY_ARG, reportRendererKey);
        outState.putFloat(SCALE_ARG, scale);
        outState.putBoolean(IN_PENDING_ARG, inPending);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (reportRenderer != null) {
            reportRenderer.unregisterReportRendererCallback();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((ViewGroup) resourceWebView.getParent()).removeView(resourceWebView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (reportRenderer != null && getActivity().isFinishing()) {
            reportRenderer.destroy();
        }
    }

    public void init(AuthorizedClient client, ServerInfo serverInfo) {
        init(client, serverInfo, 1f);
    }

    public void init(AuthorizedClient client, ServerInfo serverInfo, float scale) {
        if (reportRenderer != null) {
            throw new RuntimeException("Report fragment is already inited!");
        }
        reportRenderer = ReportRenderer.create(client, resourceWebView, serverInfo);
        this.scale = scale;
    }

    public boolean isInited() {
        return reportRenderer != null;
    }

    public boolean isControlActionsAvailable() {
        return !reportRenderer.isInProgress() && reportRenderer.getRenderState() == RenderState.RENDERED;
    }

    public boolean isFeatureSupported(ReportFeature reportFeature) {
        return reportFeature != ReportFeature.ANCHOR_NAVIGATION || reportRenderer.isFeatureSupported(ReportFeature.ANCHOR_NAVIGATION);
    }

    public void run(RunOptions runOptions) {
        checkInited();

        this.runOptions = runOptions;
        inPending = true;
        switch (reportRenderer.getRenderState()) {
            case IDLE:
                if (!reportRenderer.isInProgress()) {
                    reportRenderer.init(scale);
                }
                break;
            case INITED:
                reportRenderer.reset();
                break;
            case RENDERED:
                reportRenderer.reset();
                break;
            case DESTROYED:
                reportRenderer.render(runOptions);
                break;
        }
    }

    public void applyParams(List<ReportParameter> reportParameters) {
        checkInited();

        if (reportRenderer.isFeatureSupported(ReportFeature.PARAMS_APPLYING)) {
            reportRenderer.applyParams(reportParameters);
        } else {
            runOptions = runOptions.newBuilder().parameters(reportParameters).build();
            reportRenderer.reset();
            inPending = true;
        }
    }

    public void refresh() {
        checkInited();

        if (reportRenderer.isFeatureSupported(ReportFeature.DATA_REFRESHING)) {
            reportRenderer.refresh();
        } else {
            reportRenderer.reset();
            inPending = true;
        }
    }

    public void reset() {
        checkInited();

        reportRenderer.reset();
    }

    public void setReportFragmentEventListener(ReportFragmentEventListener reportFragmentEventListener) {
        if (reportFragmentEventListener == null) {
            reportFragmentEventListener = new ReportFragmentEventListener.SimpleReportFragmentEventListener();
        }
        this.reportFragmentEventListener = reportFragmentEventListener;
    }

    public void setPaginationView(PaginationView paginationView) {
        if (paginationView == null) {
            throw new IllegalArgumentException("PaginationView should not be null");
        }
        this.paginationView = paginationView;
        paginationView.setPaginationListener(this);
    }

    private void checkInited() {
        if (reportRenderer == null) {
            throw new IllegalStateException("Report fragment was not inited");
        }
    }

    private void updatePaginationEnabled() {
        if (paginationView != null) {
            paginationView.setEnabled(reportRenderer.getRenderState() == RenderState.RENDERED && !reportRenderer.isInProgress());
        }
    }

    @Override
    public void onNavigateTo(Destination destination) {
        reportRenderer.navigateTo(destination);
    }

    private class RendererEventListener implements ReportRendererCallback {
        @Override
        public void onProgressStateChanged(boolean inProgress) {
            resourceWebView.setVisibility(inProgress ? View.INVISIBLE : View.VISIBLE);
            updatePaginationEnabled();
            reportFragmentEventListener.onActionsAvailabilityChanged();
        }

        @Override
        public void onRenderStateChanged(RenderState renderState) {
            resourceWebView.setVisibility(renderState == RenderState.RENDERED ? View.VISIBLE : View.GONE);
            if (renderState == RenderState.INITED && inPending) {
                reportRenderer.render(runOptions);
            }

            updatePaginationEnabled();
            reportFragmentEventListener.onActionsAvailabilityChanged();
        }

        @Override
        public void onHyperlinkClicked(Hyperlink hyperlink) {
            if (hyperlink instanceof LocalHyperlink) {
                reportRenderer.navigateTo(((LocalHyperlink) hyperlink).getDestination());
            } else {
                reportFragmentEventListener.onHyperlinkClicked(hyperlink);
            }
        }

        @Override
        public void onMultiPageStateChanged(boolean isMultiPage) {
            if (paginationView != null) {
                paginationView.setVisibility(isMultiPage ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public void onCurrentPageChanged(int currentPage) {
            if (paginationView != null) {
                paginationView.onCurrentPageChanged(currentPage);
            }
        }

        @Override
        public void onPagesCountChanged(Integer totalCount) {
            if (paginationView != null) {
                paginationView.onPagesCountChanged(totalCount);
            }
            if (totalCount != null && totalCount == 0) {
                ServiceException noContentException = new ServiceException("Requested report execution has no content",
                        new Throwable("Requested report execution has no content"), StatusCodes.REPORT_EXECUTION_EMPTY);
                reportFragmentEventListener.onError(noContentException);
            }
        }

        @Override
        public void onError(ServiceException exception) {
            reportFragmentEventListener.onError(exception);
        }
    }
}
