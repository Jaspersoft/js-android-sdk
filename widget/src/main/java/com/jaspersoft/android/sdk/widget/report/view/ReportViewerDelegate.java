package com.jaspersoft.android.sdk.widget.report.view;

import android.content.Context;
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
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
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
class ReportViewerDelegate implements ReportRendererCallback, ResourceWebViewClient.WebClientEventCallback {
    private static final int SCROLL_STEP = 8;

    private ReportPaginationListener reportPaginationListener;
    private ReportEventListener reportEventListener;
    private ReportBookmarkListener reportBookmarkListener;
    private ReportPartsListener reportPartsListener;

    ReportRenderer reportRenderer;
    ResourceWebView resourceWebView;

    boolean inPending;
    RunOptions runOptions;
    ReportProperties reportProperties;

    private ReportViewerDelegate() {
        setReportEventListener(null);
        setReportPaginationListener(null);
        setReportBookmarkListener(null);
        setReportPartsListener(null);
        reportProperties = new ReportProperties();
    }

    private ReportViewerDelegate(ReportRenderer reportRenderer,
                                 ResourceWebView resourceWebView,
                                 boolean inPending,
                                 RunOptions runOptions,
                                 ReportProperties reportProperties) {
        this();
        this.reportRenderer = reportRenderer;
        this.resourceWebView = resourceWebView;
        this.inPending = inPending;
        this.runOptions = runOptions;
        this.reportProperties = reportProperties;
    }

    public static ReportViewerDelegate create() {
        return new ReportViewerDelegate();
    }

    public static ReportViewerDelegate restore(ReportRenderer reportRenderer,
                                               ResourceWebView resourceWebView,
                                               boolean inPending,
                                               RunOptions runOptions,
                                               ReportProperties reportProperties) {
        ReportViewerDelegate reportViewerDelegate = new ReportViewerDelegate(reportRenderer, resourceWebView, inPending, runOptions, reportProperties);
        reportViewerDelegate.setWebViewClient();
        return reportViewerDelegate;
    }

    public void init(AuthorizedClient client, ServerInfo serverInfo, double scale) {
        if (reportRenderer != null) {
            throw new RuntimeException("Report fragment is already inited!");
        }
        setWebViewClient();
        reportRenderer = ReportRenderer.create(client, resourceWebView, serverInfo, scale);
    }

    public void setReportEventListener(ReportEventListener reportEventListener) {
        if (reportEventListener == null) {
            reportEventListener = new ReportEventListener.SimpleReportEventListener();
        }
        this.reportEventListener = reportEventListener;
    }

    public void setReportPaginationListener(ReportPaginationListener reportPaginationListener) {
        if (reportPaginationListener == null) {
            reportPaginationListener = new ReportPaginationListener.SimpleReportPaginationListener();
        }
        this.reportPaginationListener = reportPaginationListener;
    }

    public void setReportBookmarkListener(ReportBookmarkListener reportBookmarkListener) {
        if (reportBookmarkListener == null) {
            reportBookmarkListener = new ReportBookmarkListener.SimpleReportBookmarkListener();
        }
        this.reportBookmarkListener = reportBookmarkListener;
    }

    public void setReportPartsListener(ReportPartsListener reportPartsListener) {
        if (reportPartsListener == null) {
            reportPartsListener = new ReportPartsListener.SimpleReportPartsListener();
        }
        this.reportPartsListener = reportPartsListener;
    }

    public boolean isInited() {
        return reportRenderer != null;
    }

    public boolean isControlActionsAvailable() {
        return !reportRenderer.isInProgress() && reportRenderer.getRenderState() == RenderState.RENDERED;
    }

    public void run(RunOptions runOptions) {
        checkInited();

        this.runOptions = runOptions;
        inPending = true;
        switch (reportRenderer.getRenderState()) {
            case IDLE:
                if (!reportRenderer.isInProgress()) {
                    reportRenderer.init();
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

    public void navigateToPage(int page) {
        checkInited();

        Destination pageDestination = new Destination(page);
        reportRenderer.navigateTo(pageDestination);
    }

    public void reset() {
        checkInited();

        reportRenderer.reset();
    }

    @Override
    public void onProgressStateChanged(boolean inProgress) {
        resourceWebView.setVisibility(inProgress ? View.INVISIBLE : View.VISIBLE);
        reportEventListener.onActionsAvailabilityChanged(isControlActionsAvailable());
    }

    @Override
    public void onRenderStateChanged(RenderState renderState) {
        resourceWebView.setVisibility(renderState == RenderState.RENDERED ? View.VISIBLE : View.GONE);
        if (renderState == RenderState.INITED && inPending) {
            reportRenderer.render(runOptions);
            inPending = false;
        }

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
    public void onBookmarkListAvailable(List<Bookmark> bookmarkList) {
        if (!bookmarkList.equals(reportProperties.getBookmarkList())) {
            reportProperties.setBookmarkList(bookmarkList);
            reportBookmarkListener.onBookmarkListChanged(bookmarkList);
        }
    }

    @Override
    public void onReportPartsAvailable(List<ReportPart> reportPartList) {
        if (!reportPartList.equals(reportProperties.getReportPartList())) {
            reportProperties.setReportPartList(reportPartList);
            reportPartsListener.onReportPartsChanged(reportPartList);
        }
    }

    @Override
    public void onIntercept(String uri) {
        reportEventListener.onExternalLinkOpened(uri);
    }

    @Override
    public void onMultiPageStateChanged(boolean isMultiPage) {
        reportProperties.setMultiPage(isMultiPage);
        reportPaginationListener.onMultiPageStateChange(isMultiPage);
    }

    @Override
    public void onCurrentPageChanged(int currentPage) {
        while (resourceWebView.zoomOut()) ;

        ReportPart previousReportPart = reportProperties.getCurrentReportPart();

        reportProperties.setCurrentPage(currentPage);
        reportPaginationListener.onCurrentPageChanged(currentPage);

        ReportPart currentReportPart = reportProperties.getCurrentReportPart();
        if (currentReportPart != null && !currentReportPart.equals(previousReportPart)) {
            reportPartsListener.onCurrentReportPartChanged(reportProperties.getCurrentReportPart());
        }
    }

    @Override
    public void onPagesCountChanged(Integer totalCount) {
        reportProperties.setPagesCount(totalCount);
        reportPaginationListener.onPagesCountChanged(totalCount);

        if (totalCount != null && totalCount == 0) {
            reset();
            ServiceException noContentException = new ServiceException("Requested report execution has no content",
                    new Throwable("Requested report execution has no content"), StatusCodes.REPORT_EXECUTION_EMPTY);
            reportEventListener.onError(noContentException);
        }
    }

    @Override
    public void onWebError(int errorCode, String failingUrl) {
        ServiceException serviceException = new ServiceException("WebView request error occurred", null, StatusCodes.WEB_VIEW_REQUEST_ERROR);
        onError(serviceException);
    }

    @Override
    public void onError(ServiceException exception) {
        boolean isRendered = reportRenderer.getRenderState() == RenderState.RENDERED;
        boolean isExportPageError = exception.code() >= 200 && exception.code() < 300;
        if (!isRendered || !isExportPageError) {
            reportRenderer.reset();
        }
        reportEventListener.onError(exception);
    }

    public ReportProperties getReportMetadata() {
        return reportProperties;
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

    public void registerReportRendererCallback() {
        reportRenderer.registerReportRendererCallback(this);
    }

    public void unregisterReportRendererCallback() {
        reportRenderer.unregisterReportRendererCallback();
    }

    public void createResourceView(Context context) {
        resourceWebView = new ResourceWebView(context.getApplicationContext());
    }

    public ResourceWebView getResourceView() {
        return resourceWebView;
    }

    void removeResourceView() {
        ((ViewGroup) resourceWebView.getParent()).removeView(resourceWebView);
    }

    void destroy() {
        reportRenderer.destroy();
    }

    private void checkInited() {
        if (reportRenderer == null) {
            throw new IllegalStateException("Report fragment was not inited");
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
