package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
enum RunOptionsStore {
    INSTANCE;

    Map<ReportRendererKey, RunOptions> runOptionsMap = new HashMap<>();

    public RunOptions restoreRunOptions(ReportRendererKey key){
        RunOptions reportRenderer = runOptionsMap.get(key);
        runOptionsMap.remove(key);
        return reportRenderer;
    }

    public ReportRendererKey saveRunOptions(RunOptions runOptions, ReportRendererKey key) {
        runOptionsMap.put(key, runOptions);
        return key;
    }
}
