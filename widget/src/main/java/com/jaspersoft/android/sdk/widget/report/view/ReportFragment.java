package com.jaspersoft.android.sdk.widget.report.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.ResourceWebView;
import com.jaspersoft.android.sdk.widget.ResourceWebViewStore;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRendererCallback;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeature;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportFragment extends Fragment {
    private static final String RENDERER_KEY_ARG = "rendererKey";

    private ReportRenderer reportRenderer;
    private ResourceWebView resourceWebView;
    private ReportRendererCallback reportRendererCallback;

    private float scale;
    private boolean inPending;
    private RunOptions runOptions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(RENDERER_KEY_ARG)) {
            ReportRendererKey reportRendererKey = savedInstanceState.getParcelable(RENDERER_KEY_ARG);
            reportRenderer = RenderersStore.INSTANCE.restoreExecutor(reportRendererKey);
        }

        resourceWebView = ResourceWebViewStore.getInstance().getResourceWebView();
        if (resourceWebView == null) {
            resourceWebView = new ResourceWebView(getContext().getApplicationContext());
        }

        setReportRendererCallback(null);
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
        outState.putParcelable(RENDERER_KEY_ARG, reportRendererKey);
        ResourceWebViewStore.getInstance().setWebView(resourceWebView);
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

    public boolean isInProgress() {
        return reportRenderer.isInProgress();
    }

    public boolean isFeatureSupported(ReportFeature reportFeature) {
        return reportFeature != ReportFeature.ANCHOR_NAVIGATION || reportRenderer.isFeatureSupported(ReportFeature.ANCHOR_NAVIGATION);
    }

    public RenderState getRenderState() {
        return reportRenderer.getRenderState();
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

    public void navigateTo(Destination destination) {
        checkInited();

        reportRenderer.navigateTo(destination);
    }

    public void reset() {
        checkInited();

        reportRenderer.reset();
    }

    public void setReportRendererCallback(ReportRendererCallback reportRendererCallback) {
        if (reportRendererCallback == null) {
            reportRendererCallback = new ReportRendererCallback.SimpleReportRendererCallback();
        }
        this.reportRendererCallback = reportRendererCallback;
    }

    private void checkInited() {
        if (reportRenderer == null) {
            throw new IllegalStateException("Report fragment was not inited");
        }
    }

    private class RendererEventListener implements ReportRendererCallback {
        @Override
        public void onProgressStateChanged(boolean inProgress) {
            resourceWebView.setVisibility(inProgress ? View.INVISIBLE : View.VISIBLE);
            reportRendererCallback.onProgressStateChanged(inProgress);
        }

        @Override
        public void onRenderStateChanged(RenderState renderState) {
            resourceWebView.setVisibility(renderState == RenderState.RENDERED ? View.VISIBLE : View.GONE);
            if (renderState == RenderState.INITED && inPending) {
                reportRenderer.render(runOptions);
            }
            reportRendererCallback.onRenderStateChanged(renderState);
        }

        @Override
        public void onHyperlinkClicked(Hyperlink hyperlink) {
            reportRendererCallback.onHyperlinkClicked(hyperlink);

        }

        @Override
        public void onCurrentPageChanged(int currentPage) {
            Toast.makeText(getContext(), "Current " + currentPage, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPagesCountChanged(int totalCount) {
            Toast.makeText(getContext(), "Total " + totalCount, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(ServiceException exception) {
            reportRendererCallback.onError(exception);
        }
    }
}
