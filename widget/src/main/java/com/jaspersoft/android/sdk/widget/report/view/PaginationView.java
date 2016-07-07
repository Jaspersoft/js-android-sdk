package com.jaspersoft.android.sdk.widget.report.view;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public interface PaginationView {
    void onPagesCountChanged(int totalPages);
    void onCurrentPageChanged(int currentPage);
}
