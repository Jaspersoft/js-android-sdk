package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.base.ResourceWebView;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class ReportWebViewStore extends Store<ResourceWebView> {
    private static ReportWebViewStore instance = new ReportWebViewStore();

    public static ReportWebViewStore getInstance() {
        return instance;
    }
}
