package com.jaspersoft.android.sdk.widget.report.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import com.jaspersoft.android.sdk.widget.base.ResourceWebView;
import com.jaspersoft.android.sdk.widget.base.ResourceWebViewClient;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.RenderState;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportRendererCallback;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;
import com.jaspersoft.android.sdk.widget.report.renderer.compat.ReportFeature;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.LocalHyperlink;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ReportViewerDelegate implements PaginationView.PaginationListener, ReportRendererCallback, ResourceWebViewClient.WebClientEventCallback {
    private static final int SCROLL_STEP = 8;
    private static final String RENDERER_KEY_ARG = "rendererKey";
    private static final String SCALE_ARG = "scaleKey";
    private static final String IN_PENDING_ARG = "inPendingKey";

    private ReportRenderer reportRenderer;
    private ResourceWebView resourceWebView;
    private PaginationView paginationView;
    private ReportEventListener reportEventListener;

    private float scale;
    private boolean inPending;
    private RunOptions runOptions;

    private ReportViewerDelegate() {
        setReportEventListener(null);
    }

    public static ReportViewerDelegate create() {
        return new ReportViewerDelegate();
    }

    public void init(AuthorizedClient client, ServerInfo serverInfo, float scale) {
        if (reportRenderer != null) {
            throw new RuntimeException("Report fragment is already inited!");
        }
        setWebViewClient();
        reportRenderer = ReportRenderer.create(client, resourceWebView, serverInfo);
        this.scale = scale;
    }

    public void setReportEventListener(ReportEventListener reportEventListener) {
        if (reportEventListener == null) {
            reportEventListener = new ReportEventListener.SimpleReportEventListener();
        }
        this.reportEventListener = reportEventListener;
    }

    public void setPaginationView(PaginationView paginationView) {
        if (paginationView == null) {
            throw new IllegalArgumentException("PaginationView should not be null");
        }
        this.paginationView = paginationView;
        paginationView.setPaginationListener(this);
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

    @Override
    public void onProgressStateChanged(boolean inProgress) {
        resourceWebView.setVisibility(inProgress ? View.INVISIBLE : View.VISIBLE);
        updatePaginationEnabled();
        reportEventListener.onActionsAvailabilityChanged(isControlActionsAvailable());
    }

    @Override
    public void onRenderStateChanged(RenderState renderState) {
        resourceWebView.setVisibility(renderState == RenderState.RENDERED ? View.VISIBLE : View.GONE);
        if (renderState == RenderState.INITED && inPending) {
            reportRenderer.render(runOptions);
        }

        updatePaginationEnabled();
        reportEventListener.onActionsAvailabilityChanged(isControlActionsAvailable());
    }

    @Override
    public void onHyperlinkClicked(Hyperlink hyperlink) {
        if (hyperlink instanceof LocalHyperlink) {
            Destination destination = ((LocalHyperlink) hyperlink).getDestination();
            if (destination != null) {
                reportRenderer.navigateTo(destination);
            }
        } else {
            reportEventListener.onHyperlinkClicked(hyperlink);
        }
    }

    @Override
    public void onBookmarkListChanged(List<Bookmark> bookmarkList) {
        if (bookmarkList == null) {
            reportEventListener.onBookmarkListChanged(new ArrayList<Bookmark>());
        } else {
            reportEventListener.onBookmarkListChanged(bookmarkList);
        }
    }

    @Override
    public void onIntercept(String uri) {
        reportEventListener.onExternalLinkOpened(uri);
    }

    @Override
    public void onMultiPageStateChanged(boolean isMultiPage) {
        if (paginationView != null) {
            paginationView.show(isMultiPage);
        }
    }

    @Override
    public void onCurrentPageChanged(int currentPage) {
        while (resourceWebView.zoomOut()) ;
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
            reportEventListener.onError(noContentException);
            onMultiPageStateChanged(false);
        }
    }

    @Override
    public void onWebError(int errorCode, String failingUrl) {
        ServiceException serviceException = new ServiceException("WebView request error occurred", null, StatusCodes.WEB_VIEW_REQUEST_ERROR);
        onError(serviceException);
    }

    @Override
    public void onError(ServiceException exception) {
        reportEventListener.onError(exception);
    }

    @Override
    public void onNavigateTo(Destination destination) {
        reportRenderer.navigateTo(destination);
    }

    public List<Bookmark> getBookmarks() {
        List<Bookmark> bookmarkList = reportRenderer.getBookmarkList();
        return bookmarkList == null ? new ArrayList<Bookmark>() : bookmarkList;
    }

    public void performViewAction(ViewAction viewAction) {
        switch (viewAction) {
            case ZOOM_IN:
                resourceWebView.zoomIn();
                break;
            case ZOOM_OUT:
                resourceWebView.zoomOut();
                break;
            case SCROLL_LEFT:
                scrollHorizontal(-SCROLL_STEP);
                break;
            case SCROLL_RIGHT:
                scrollHorizontal(SCROLL_STEP);
                break;
            case SCROLL_UP:
                scrollVertical(-SCROLL_STEP);
                break;
            case SCROLL_DOWN:
                scrollVertical(SCROLL_STEP);
                break;
        }
    }

    void registerReportRendererCallback() {
        reportRenderer.registerReportRendererCallback(this);
    }

    void unregisterReportRendererCallback() {
        reportRenderer.unregisterReportRendererCallback();
    }

    ReportRendererKey saveInStore() {
        ReportRendererKey reportRendererKey = ReportRendererKey.newKey();
        RenderersStore.getInstance().save(reportRenderer, reportRendererKey);
        ReportWebViewStore.getInstance().save(resourceWebView, reportRendererKey);
        RunOptionsStore.getInstance().save(runOptions, reportRendererKey);
        return reportRendererKey;
    }

    void persistData(Bundle bundle, ReportRendererKey reportRendererKey) {
        if (reportRendererKey == null) return;

        bundle.putParcelable(RENDERER_KEY_ARG, reportRendererKey);
        bundle.putFloat(SCALE_ARG, scale);
        bundle.putBoolean(IN_PENDING_ARG, inPending);
    }

    ReportRendererKey restoreData(Bundle bundle) {
        if (!bundle.containsKey(RENDERER_KEY_ARG)) return null;

        scale = bundle.getFloat(SCALE_ARG);
        inPending = bundle.getBoolean(IN_PENDING_ARG);
        ReportRendererKey reportRendererKey = bundle.getParcelable(RENDERER_KEY_ARG);

        reportRenderer = RenderersStore.getInstance().restore(reportRendererKey);
        resourceWebView = ReportWebViewStore.getInstance().restore(reportRendererKey);
        runOptions = RunOptionsStore.getInstance().restore(reportRendererKey);

        setWebViewClient();

        return reportRendererKey;
    }

    void createResourceView(Context context) {
        resourceWebView = new ResourceWebView(context.getApplicationContext());
    }

    ResourceWebView getResourceView() {
        return resourceWebView;
    }

    void removeResourceView() {
        ((ViewGroup) resourceWebView.getParent()).removeView(resourceWebView);
    }

    void destroy(ReportRendererKey reportRendererKey) {
        reportRenderer.destroy();
        if (reportRendererKey != null) {
            RenderersStore.getInstance().remove(reportRendererKey);
            ReportWebViewStore.getInstance().remove(reportRendererKey);
            RunOptionsStore.getInstance().remove(reportRendererKey);
        }
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

    private void scrollVertical(int scrollValue) {
        if (resourceWebView.canScrollVertically(scrollValue)) {
            resourceWebView.scrollBy(0, scrollValue);
        }
    }

    private void scrollHorizontal(int scrollValue) {
        if (resourceWebView.canScrollHorizontally(scrollValue)) {
            resourceWebView.scrollBy(scrollValue, 0);
        }
    }

    private void setWebViewClient() {
        resourceWebView.setWebViewClient(new ResourceWebViewClient.Builder()
                .withEventListener(this)
                .build(resourceWebView.getContext()));
    }
}
