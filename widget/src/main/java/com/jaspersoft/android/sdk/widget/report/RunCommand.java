package com.jaspersoft.android.sdk.widget.report;

import com.jaspersoft.android.sdk.widget.RunOptions;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RunCommand implements Command {
    private final RunOptions options;
    private final double version;

    public RunCommand(RunOptions options, double version) {
        this.options = options;
        this.version = version;
    }

    public RunOptions getOptions() {
        return options;
    }

    public double getVersion() {
        return version;
    }
}
