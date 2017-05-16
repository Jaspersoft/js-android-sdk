package com.jaspersoft.android.sdk.widget.report.view;

import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
class RunOptionsStore extends Store<RunOptions>{
    private static RunOptionsStore instance = new RunOptionsStore();

    public static RunOptionsStore getInstance() {
        return instance;
    }
}
