package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.report.renderer.ReportRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
enum RenderersStore {
    INSTANCE;

    Map<ReportRendererKey, ReportRenderer> reportExecutorList = new HashMap<>();

    public ReportRenderer restoreExecutor(ReportRendererKey key){
        ReportRenderer reportRenderer = reportExecutorList.get(key);
        reportExecutorList.remove(key);
        return reportRenderer;
    }

    public ReportRendererKey saveExecutor(ReportRenderer reportRenderer) {
        ReportRendererKey key = ReportRendererKey.newKey();
        reportExecutorList.put(key, reportRenderer);
        return key;
    }
}
