package com.jaspersoft.android.sdk.widget.report.view;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface ReportPaginationListener {
    void onPagesCountChanged(Integer totalPages);

    void onCurrentPageChanged(int currentPage);

    void onMultiPageStateChange(boolean isMultiPage);

    class SimpleReportPaginationListener implements ReportPaginationListener{

        @Override
        public void onPagesCountChanged(Integer totalPages) {

        }

        @Override
        public void onCurrentPageChanged(int currentPage) {

        }

        @Override
        public void onMultiPageStateChange(boolean isMultiPage) {

        }
    }
}
