package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class RenderersStore extends Store<ReportRenderer> {
    private static RenderersStore instance = new RenderersStore();

    public static RenderersStore getInstance() {
        return instance;
    }
}
