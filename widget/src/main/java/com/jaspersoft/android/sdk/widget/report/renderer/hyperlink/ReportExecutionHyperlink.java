package com.jaspersoft.android.sdk.widget.report.renderer.hyperlink;

import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportExecutionHyperlink implements Hyperlink {
    private final RunOptions runOptions;

    public ReportExecutionHyperlink(RunOptions runOptions) {
        this.runOptions = runOptions;
    }

    public RunOptions getRunOptions() {
        return runOptions;
    }
}
