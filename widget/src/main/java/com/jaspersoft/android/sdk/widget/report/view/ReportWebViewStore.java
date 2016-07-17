package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.base.ResourceWebView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public enum ReportWebViewStore {
    INSTANCE;

    Map<ReportRendererKey, ResourceWebView> resourceWebViewMap = new HashMap<>();

    public ResourceWebView restoreReportView(ReportRendererKey key) {
        ResourceWebView reportRenderer = resourceWebViewMap.get(key);
        resourceWebViewMap.remove(key);
        return reportRenderer;
    }

    public ReportRendererKey saveReportView(ResourceWebView resourceWebView, ReportRendererKey key) {
        resourceWebViewMap.put(key, resourceWebView);
        return key;
    }
}
