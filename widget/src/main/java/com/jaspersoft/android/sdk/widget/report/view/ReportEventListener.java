package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;
import com.jaspersoft.android.sdk.widget.report.renderer.hyperlink.Hyperlink;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportEventListener {
    void onActionsAvailabilityChanged(boolean isAvailable);
    void onHyperlinkClicked(Hyperlink hyperlink);
    void onExternalLinkOpened(String url);
    void onError(ServiceException exception);
    void onBookmarkListChanged(List<Bookmark> bookmarks);
    void onReportPartsChanged(List<ReportPart> reportPartList);

    class SimpleReportEventListener implements ReportEventListener {
        @Override
        public void onActionsAvailabilityChanged(boolean isAvailable) {

        }

        @Override
        public void onHyperlinkClicked(Hyperlink hyperlink) {

        }

        @Override
        public void onExternalLinkOpened(String url) {

        }

        @Override
        public void onError(ServiceException exception) {

        }

        @Override
        public void onBookmarkListChanged(List<Bookmark> bookmarks) {

        }

        @Override
        public void onReportPartsChanged(List<ReportPart> reportPartList) {

        }
    }
}
