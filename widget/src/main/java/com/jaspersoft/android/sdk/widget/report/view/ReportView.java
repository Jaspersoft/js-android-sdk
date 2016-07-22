package com.jaspersoft.android.sdk.widget.report.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportView extends FrameLayout implements ReportWidget {
    private ReportViewerDelegate reportViewerDelegate;

    public ReportView(Context context) {
        super(context);
        create();
    }

    public ReportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        create();
    }

    public ReportView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        create();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReportView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        create();
    }

    private void create() {
        reportViewerDelegate = ReportViewerDelegate.create();
        reportViewerDelegate.createResourceView(getContext());
        addView(reportViewerDelegate.getResourceView());
    }

    @Override
    public void init(AuthorizedClient client, ServerInfo serverInfo, float scale) {
        reportViewerDelegate.init(client, serverInfo, scale);
        reportViewerDelegate.registerReportRendererCallback();
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
    public void navigateToBookmark(Bookmark bookmark) {
        reportViewerDelegate.navigateToBookmark(bookmark);
    }

    @Override
    public void navigateToReportPart(ReportPart reportPart) {
        reportViewerDelegate.navigateToReportPart(reportPart);
    }

    @Override
    public List<Bookmark> getBookmarks() {
        return reportViewerDelegate.getBookmarks();
    }

    @Override
    public List<ReportPart> getReportParts() {
        return reportViewerDelegate.getReportParts();
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
    public View getView() {
        return this;
    }

    @Override
    public void performViewAction(ViewAction viewAction) {
        reportViewerDelegate.performViewAction(viewAction);
    }
}
