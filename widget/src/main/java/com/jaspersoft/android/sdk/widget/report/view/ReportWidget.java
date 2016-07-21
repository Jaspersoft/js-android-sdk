package com.jaspersoft.android.sdk.widget.report.view;

import android.view.View;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportWidget {

    void init(AuthorizedClient client, ServerInfo serverInfo, float scale);

    boolean isInited();

    boolean isControlActionsAvailable();

    void setReportEventListener(ReportEventListener reportEventListener);

    void setPaginationView(PaginationView paginationView);

    View getView();

    void run(RunOptions runOptions);

    void applyParams(List<ReportParameter> reportParameters);

    void refresh();

    void navigateToBookmark(Bookmark bookmark);

    void performViewAction(ViewAction viewAction);

    List<Bookmark> getBookmarks();

    void reset();
}
