package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportBookmarkListener {
    void onBookmarkListChanged(List<Bookmark> bookmarkList);

    class SimpleReportBookmarkListener implements ReportBookmarkListener {
        @Override
        public void onBookmarkListChanged(List<Bookmark> bookmarkList) {

        }
    }
}
