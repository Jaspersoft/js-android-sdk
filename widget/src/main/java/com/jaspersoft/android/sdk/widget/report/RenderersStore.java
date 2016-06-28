package com.jaspersoft.android.sdk.widget.report;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
enum RenderersStore {
    INSTANCE;

    Map<ReportRendererKey, ReportRendered> reportExecutorList = new HashMap<>();

    public ReportRendered restoreExecutor(ReportRendererKey key){
        ReportRendered reportRendered = reportExecutorList.get(key);
        reportExecutorList.remove(key);
        return reportRendered;
    }

    public ReportRendererKey saveExecutor(ReportRendered reportRendered) {
        ReportRendererKey key = ReportRendererKey.newKey();
        reportExecutorList.put(key, reportRendered);
        return key;
    }
}
