package com.jaspersoft.android.sdk.widget.report.renderer.hyperlink;

import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportExecutionHyperlink implements Hyperlink {
    private final RunOptions runOptions;
    private final String reportFormat;

    public ReportExecutionHyperlink(RunOptions runOptions, String reportFormat) {
        this.runOptions = runOptions;
        this.reportFormat = reportFormat;
    }

    public RunOptions getRunOptions() {
        return runOptions;
    }

    public String getReportFormat() {
        return reportFormat;
    }
}
