package com.jaspersoft.android.sdk.widget.dashboard;

/**
 * @author Tom Koptel
 * @since 2.6
 */
class RunCommand implements Command {
    private final RunOptions options;

    public RunCommand(RunOptions options) {
        this.options = options;
    }

    public RunOptions getOptions() {
        return options;
    }
}
